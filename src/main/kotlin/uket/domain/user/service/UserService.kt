package uket.domain.user.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import uket.common.ErrorLevel
import uket.common.PublicException
import uket.domain.user.dto.CreateUserCommand
import uket.domain.user.dto.RegisterUserCommand
import uket.domain.user.entity.User
import uket.domain.user.repository.UserRepository

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
) {
    /*
        유저 조회
     */
    fun getById(userId: Long): User {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw IllegalStateException("해당 사용자를 찾을 수 없습니다.")
        return user
    }

    /*
        유저 생성 또는 업데이트
     */
    @Transactional(propagation = Propagation.REQUIRED)
    fun createUser(createUserCommand: CreateUserCommand): User {
        val existUser = userRepository.findByPlatformAndPlatformId(
            createUserCommand.platform,
            createUserCommand.platformId,
        )

        if (existUser != null) {
            updateProfileOfExistUser(createUserCommand, existUser)
            return existUser
        }

        val newUser = User(
            platform = createUserCommand.platform,
            platformId = createUserCommand.platformId,
            name = createUserCommand.name,
            email = createUserCommand.email,
            profileImage = createUserCommand.profileImage,
        )

        return runCatching { userRepository.save(newUser) }
            .getOrElse { e ->
                throw PublicException(
                    publicMessage = "로그인 중 오류가 발생했습니다 재시도 해주세요",
                    systemMessage = "[UserService] 유저 동시 생성 | platform : ${newUser.platform}, platformId : ${newUser.platformId}",
                    title = "유저 동시 생성 시도",
                    errorLevel = ErrorLevel.DEBUG
                )
            }
    }

    /*
        유저 정보 등록
     */
    @Transactional(propagation = Propagation.REQUIRED)
    fun registerUser(registerUserCommand: RegisterUserCommand) {
        val user = this.getById(registerUserCommand.userId)
        user.register(registerUserCommand.depositorName, registerUserCommand.phoneNumber)
    }

    /*
        유저 정보 수정
     */
    @Transactional(propagation = Propagation.REQUIRED)
    fun updateUserInfo(
        userId: Long,
        email: String?,
        name: String?,
        profileImage: String?,
        depositorName: String?,
        phoneNumber: String?,
    ): User {
        val user = userRepository.findById(userId).get()
        user.updateEntireUserInfo(
            email = email,
            name = name,
            profileImage = profileImage,
            depositorName = depositorName,
            phoneNumber = phoneNumber
        )
        return userRepository.save(user)
    }

    /*
        유저 삭제
     */
    @Transactional
    fun deleteUser(userId: Long) {
        val user = this.getById(userId)
        userRepository.delete(user)
    }

    /*
        메일 중복 확인
     */
    fun checkDuplicateEmail(email: String) {
        check(userRepository.existsByEmail(email).not()) {
            throw IllegalStateException("이미 가입된 사용자입니다.")
        }
    }

    private fun updateProfileOfExistUser(createUserCommand: CreateUserCommand, existUser: User) {
        existUser.updateProfile(createUserCommand.email, createUserCommand.name, createUserCommand.profileImage)
        userRepository.save(existUser)
    }
}
