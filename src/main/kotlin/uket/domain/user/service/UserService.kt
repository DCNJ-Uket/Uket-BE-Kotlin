package uket.uket.domain.user.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.uket.common.ErrorCode
import uket.uket.domain.user.dto.CreateUserDto
import uket.uket.domain.user.dto.RegisterUserDto
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
    fun createUser(createUserDto: CreateUserDto) {
        val existUser = userRepository.findByPlatformAndPlatformId(
            createUserDto.platform,
            createUserDto.platformId,
        )

        if (existUser != null) {
            updateProfileOfExistUser(createUserDto, existUser)
        }

        val newUser: User = User(
            _id = 0L,
            platform = createUserDto.platform,
            platformId = createUserDto.platformId,
            name = createUserDto.name,
            email = createUserDto.email,
            profileImage = createUserDto.profileImage,
        )

        userRepository.save(newUser)
    }

    /*
        유저 정보 등록
     */
    @Transactional
    fun register(registerUserDto: RegisterUserDto) {
        val user = userRepository.findByIdOrNull(registerUserDto.userId)
            ?: throw UserException(ErrorCode.NOT_FOUND_USER)
        user.register(registerUserDto.depositorName, registerUserDto.phoneNumber)
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

    private fun updateProfileOfExistUser(createUserDto: CreateUserDto, existUser: User) {
        existUser.updateProfile(createUserDto.email, createUserDto.name, createUserDto.profileImage)
        userRepository.save(existUser)
    }
}
