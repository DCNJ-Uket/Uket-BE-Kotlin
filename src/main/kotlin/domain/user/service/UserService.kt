package uket.domain.user.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
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
    @Transactional
    fun createUser(createUserCommand: CreateUserCommand): User {
        val existUser = userRepository.findByPlatformAndPlatformId(
            createUserCommand.platform,
            createUserCommand.platformId,
        )

        if (existUser != null) {
            updateProfileOfExistUser(createUserCommand, existUser)
        }

        val newUser: User = User(
            platform = createUserCommand.platform,
            platformId = createUserCommand.platformId,
            name = createUserCommand.name,
            email = createUserCommand.email,
            profileImage = createUserCommand.profileImage,
        )

        return userRepository.save(newUser)
    }

    /*
        유저 정보 등록
     */
    @Transactional
    fun registerUser(registerUserCommand: RegisterUserCommand) {
        val user = this.getById(registerUserCommand.userId)
        user.register(registerUserCommand.depositorName, registerUserCommand.phoneNumber)
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
