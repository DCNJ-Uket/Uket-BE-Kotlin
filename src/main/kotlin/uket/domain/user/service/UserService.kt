package uket.uket.domain.user.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.uket.common.ErrorCode
import uket.uket.domain.user.dto.CreateUserCommand
import uket.uket.domain.user.dto.RegisterUserCommand
import uket.uket.domain.user.entity.User
import uket.uket.domain.user.exception.UserException
import uket.uket.domain.user.repository.UserRepository

@Service
@Transactional(readOnly = true)
class UserService(
    val userRepository: UserRepository,
) {
    /*
        유저 조회
     */
    fun findById(userId: Long): User {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw UserException(ErrorCode.NOT_FOUND_USER)
        return user
    }

    /*
        유저 생성 또는 업데이트
     */
    @Transactional
    fun createUser(createUserCommand: CreateUserCommand) {
        val existUser = userRepository.findByPlatformAndPlatformId(
            createUserCommand.platform,
            createUserCommand.platformId,
        )

        if (existUser != null) {
            updateProfileOfExistUser(createUserCommand, existUser)
        }

        val newUser: User = User(
            _id = 0L,
            platform = createUserCommand.platform,
            platformId = createUserCommand.platformId,
            name = createUserCommand.name,
            email = createUserCommand.email,
            profileImage = createUserCommand.profileImage,
        )

        userRepository.save(newUser)
    }

    /*
        유저 정보 등록
     */
    @Transactional
    fun register(registerUserCommand: RegisterUserCommand) {
        val user = userRepository.findByIdOrNull(registerUserCommand.userId)
            ?: throw UserException(ErrorCode.NOT_FOUND_USER)
        user.register(registerUserCommand.depositorName, registerUserCommand.phoneNumber)
    }

    /*
        유저 삭제
     */
    @Transactional
    fun deleteUser(userId: Long) {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw UserException(ErrorCode.NOT_FOUND_USER)
        userRepository.delete(user)
    }

    /*
        메일 중복 확인
     */
    fun checkDuplicateEmail(email: String) {
        if (java.lang.Boolean.TRUE == userRepository.existsByEmail(email)) {
            throw UserException(ErrorCode.ALREADY_EXIST_USER)
        }
    }

    private fun updateProfileOfExistUser(createUserCommand: CreateUserCommand, existUser: User) {
        existUser.updateProfile(createUserCommand.email, createUserCommand.name, createUserCommand.profileImage)
        userRepository.save(existUser)
    }
}
