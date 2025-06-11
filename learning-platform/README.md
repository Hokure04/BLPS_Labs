```mermaid
classDiagram
direction BT
class AdminPanelController {
  + AdminPanelController(AdminPanelServiceImpl) 
  + updateRole(Map~String, String~) void
}
class AdminPanelService {
<<Interface>>
  + updateRole(String, String) void
}
class AdminPanelServiceImpl {
  + AdminPanelServiceImpl(UserService, PlatformTransactionManager) 
  + updateRole(String, String) void
}
class Application {
  + Application() 
  + Application(Long, ApplicationStatus, User, NewCourse, Timestamp, Timestamp) 
  - User user
  - NewCourse newCourse
  - Timestamp createdAt
  - Timestamp updatedAt
  - Long id
  - ApplicationStatus status
  + hashCode() int
  + updateTimestamps() void
  + createTimeStamps() void
  + equals(Object) boolean
  + toString() String
  + builder() ApplicationBuilder
  # canEqual(Object) boolean
   NewCourse newCourse
   ApplicationStatus status
   Long id
   Timestamp createdAt
   User user
   Timestamp updatedAt
}
class ApplicationBuilder {
  ~ ApplicationBuilder() 
  + status(ApplicationStatus) ApplicationBuilder
  + user(User) ApplicationBuilder
  + newCourse(NewCourse) ApplicationBuilder
  + toString() String
  + createdAt(Timestamp) ApplicationBuilder
  + build() Application
  + id(Long) ApplicationBuilder
  + updatedAt(Timestamp) ApplicationBuilder
}
class ApplicationController {
  + ApplicationController(UserEnrollmentServiceImpl, ApplicationServiceImpl) 
  + updateApplicationStatus(Long, Map~String, String~) void
  + createApplication(UUID) Long
}
class ApplicationRepository {
<<Interface>>
  + findByNewCourse_Uuid(UUID) List~Application~
  + deleteAllByNewCourse_Uuid(UUID) void
  + findByUser_Id(Long) List~Application~
}
class ApplicationResponseDto {
  + ApplicationResponseDto() 
  + ApplicationResponseDto(String, BigDecimal, JwtAuthenticationResponse, Long) 
  - String description
  - Long applicationID
  - JwtAuthenticationResponse jwt
  - BigDecimal price
  + toString() String
  + equals(Object) boolean
  + hashCode() int
  # canEqual(Object) boolean
  + builder() ApplicationResponseDtoBuilder
   String description
   Long applicationID
   BigDecimal price
   JwtAuthenticationResponse jwt
}
class ApplicationResponseDtoBuilder {
  ~ ApplicationResponseDtoBuilder() 
  + toString() String
  + jwt(JwtAuthenticationResponse) ApplicationResponseDtoBuilder
  + price(BigDecimal) ApplicationResponseDtoBuilder
  + build() ApplicationResponseDto
  + description(String) ApplicationResponseDtoBuilder
  + applicationID(Long) ApplicationResponseDtoBuilder
}
class ApplicationService {
<<Interface>>
  + add(UUID) Application
  + add(UUID, User) Application
  + updateStatus(Long, ApplicationStatus) Application
  + find(UUID) List~Application~
  + getByUserId(Long) List~Application~
  + remove(List~Application~) void
}
class ApplicationServiceImpl {
  + ApplicationServiceImpl(ApplicationRepository, UserService, NewCourseService, PlatformTransactionManager) 
  + getByUserId(Long) List~Application~
  + updateStatus(Long, ApplicationStatus) Application
  + find(UUID) List~Application~
  + add(UUID, User) Application
  + add(UUID) Application
  + remove(List~Application~) void
   User currentUser
}
class ApplicationStatus {
<<enumeration>>
  - ApplicationStatus() 
  + values() ApplicationStatus[]
  + valueOf(String) ApplicationStatus
}
class ApplicationStatusAlreadySetException {
  + ApplicationStatusAlreadySetException(String) 
}
class AuthController {
  + AuthController(AuthService) 
  + signUp(RegistrationRequestDto, UUID) ApplicationResponseDto
  + signUp(RegistrationRequestDto) JwtAuthenticationResponse
  + signIn(LoginRequest) JwtAuthenticationResponse
}
class AuthService {
<<Interface>>
  + signUp(RegistrationRequestDto) JwtAuthenticationResponse
  + signUp(RegistrationRequestDto, UUID) ApplicationResponseDto
  + signIn(LoginRequest) JwtAuthenticationResponse
   User currentUser
}
class AuthServiceImpl {
  + AuthServiceImpl(NewCourseService, PasswordEncoder, JwtService, UserService, ApplicationService, StudentRepository, PlatformTransactionManager) 
  + signIn(LoginRequest) JwtAuthenticationResponse
  - getUserOrThrow(RegistrationRequestDto) User
  + signUp(RegistrationRequestDto) JwtAuthenticationResponse
  - getStudentOrThrow(User) Student
  + signUp(RegistrationRequestDto, UUID) ApplicationResponseDto
   User currentUser
}
class AuthorizeException {
  + AuthorizeException(String) 
}
class Bootstrap {
  + Bootstrap(AuthService, NewCourseService, UserService, ApplicationService, EntityManager, NewModuleService, NewExerciseService) 
  + run(String[]) void
}
class CamundaUtils {
  + CamundaUtils() 
  + getVariableOrNull(DelegateExecution, String) T
  + prepareVariables(DelegateExecution) void
  + prepareErrorVariables(DelegateExecution) void
  + getVariableString(DelegateExecution, String) String
}
class CasualAuth {
  + CasualAuth(UserService, PasswordEncoder, IdentityService) 
  + execute(DelegateExecution) void
}
class CertificateController {
  + CertificateController(AuthService, CertificateManager) 
  + getMethodName(UUID) ResponseEntity~Map~String, String~~
  + dolbaeb() void
}
class CertificateGeneratedEvent {
  + CertificateGeneratedEvent(User, NewCourse, File) 
  - User user
  - NewCourse course
  - File pdf
  + toString() String
  # canEqual(Object) boolean
  + equals(Object) boolean
  + hashCode() int
   User user
   File pdf
   NewCourse course
}
class CertificateGenerationFailedEvent {
  + CertificateGenerationFailedEvent(User, NewCourse, Exception) 
  - User user
  - NewCourse course
  - Exception exception
  + equals(Object) boolean
  + toString() String
  + hashCode() int
  # canEqual(Object) boolean
   Exception exception
   User user
   NewCourse course
}
class CertificateGenerator {
<<Interface>>
  + generateCertificate(String, String, String) File
}
class CertificateManager {
<<Interface>>
  + getCertificate(User, UUID) void
}
class CertificateManagerImpl {
  + CertificateManagerImpl(NewCourseService, ApplicationEventPublisher) 
  + getCertificate(User, UUID) void
}
class CertificatePdfGenerator {
  + CertificatePdfGenerator() 
  + generateCertificate(String, String, String) File
}
class CertificateSentEvent {
  + CertificateSentEvent(User, File) 
  - User user
  - File pdf
  # canEqual(Object) boolean
  + equals(Object) boolean
  + toString() String
  + hashCode() int
   User user
   File pdf
}
class Codes {
<<enumeration>>
  - Codes(String) 
  - String stringName
  + valueOf(String) Codes
  + values() Codes[]
   String stringName
}
class CompanyDto {
  ~ CompanyDto(String) 
  - String companyName
  # canEqual(Object) boolean
  + equals(Object) boolean
  + hashCode() int
  + toString() String
  + builder() CompanyDtoBuilder
   String companyName
}
class CompanyDtoBuilder {
  ~ CompanyDtoBuilder() 
  + companyName(String) CompanyDtoBuilder
  + build() CompanyDto
  + toString() String
}
class CourseCompletedEvent {
  + CourseCompletedEvent(User, NewCourse) 
  - NewCourse course
  - User user
  + toString() String
  # canEqual(Object) boolean
  + hashCode() int
  + equals(Object) boolean
   User user
   NewCourse course
}
class CourseController {
  + CourseController(NewCourseService) 
  + createCourse(NewCourseDto) ResponseEntity~Map~String, Object~~
  + deleteCourse(UUID) ResponseEntity~Map~String, Object~~
  + updateCourse(UUID, NewCourseDto) ResponseEntity~Map~String, Object~~
  + addAdditionalCourse(UUID, UUID) ResponseEntity~Map~String, Object~~
  + linkExerciseToModule(UUID, UUID) ResponseEntity~Map~String, Object~~
}
class CourseController {
  + CourseController(NewCourseService, AuthService) 
  + completeCourse(UUID) ResponseEntity~Map~String, Object~~
  + getCourseById(UUID) ResponseEntity~Map~String, Object~~
   ResponseEntity~Map~String, Object~~ allCourses
}
class CourseProgressId {
  + CourseProgressId() 
  + CourseProgressId(Long, Long) 
  + equals(Object) boolean
  # canEqual(Object) boolean
  + hashCode() int
}
class CourseProgressService {
<<Interface>>
  + addPoints(Long, Long, int) void
}
class CustomAuthorityDeserializer {
  + CustomAuthorityDeserializer() 
  + deserialize(JsonParser, DeserializationContext) Object
}
class EmailService {
<<Interface>>
  + informAboutModuleCompletion(String, String, String) void
  + informMinioFailure(String) void
  + informAboutNewCourses(String, String, BigDecimal, List~NewCourse~) void
  + rejectionMail(String, String) void
  + createMimeMessageHelper(String, String) MimeMessageHelper
  + sendCertificateToUser(String, File) void
}
class EmailServiceImpl {
  + EmailServiceImpl(JavaMailSender) 
  + createMimeMessageHelper(String, String) MimeMessageHelper
  + rejectionMail(String, String) void
  + informMinioFailure(String) void
  + informAboutNewCourses(String, String, BigDecimal, List~NewCourse~) void
  + sendCertificateToUser(String, File) void
  + informAboutModuleCompletion(String, String, String) void
  + informAboutCourseCompletion(String, String) void
  + informAboutCompanyProblem(String, String) void
}
class EmailServiceStub {
  + EmailServiceStub() 
  + rejectionMail(String, String) void
  + informAboutModuleCompletion(String, String, String) void
  + createMimeMessageHelper(String, String) MimeMessageHelper
  - simulateWorking() void
  + informMinioFailure(String) void
  + sendCertificateToUser(String, File) void
  + informAboutNewCourses(String, String, BigDecimal, List~NewCourse~) void
}
class EnrollmentController {
  + EnrollmentController(NewCourseService) 
  + enrollUser(Long, UUID) List~NewCourse~
}
class ExceptionWrapper {
  + ExceptionWrapper(Exception) 
  - String message
  - String time
  # canEqual(Object) boolean
  + equals(Object) boolean
  + toString() String
  + hashCode() int
   String message
   String time
}
class ExerciseController {
  + ExerciseController(NewExerciseService) 
  + updateExercise(UUID, NewExerciseDto) ResponseEntity~Map~String, Object~~
  + createExercise(NewExerciseDto) ResponseEntity~Map~String, Object~~
  + getExerciseById(UUID) ResponseEntity~Map~String, Object~~
  + deleteExercise(UUID) ResponseEntity~Map~String, Object~~
   ResponseEntity~Map~String, Object~~ exercises
}
class ExerciseController {
  + ExerciseController(NewExerciseService) 
  + submitAnswer(UUID, Map~String, String~) ResponseEntity~Map~String, Object~~
}
class FailedToMapException {
  + FailedToMapException(String) 
}
class FailureRecord {
  + FailureRecord(Long, String, String, NewCourse, SagaFailedStep, String, Instant, Instant) 
  + FailureRecord() 
  - String userPassword
  - Long id
  - SagaFailedStep sagaFailedStep
  - NewCourse course
  - Instant updatedAt
  - String errorMessage
  - String username
  - Instant createdAt
  + toString() String
  + hashCode() int
  + builder() FailureRecordBuilder
  + equals(Object) boolean
  # canEqual(Object) boolean
  + prePersist() void
  + preUpdate() void
   SagaFailedStep sagaFailedStep
   String userPassword
   Long id
   NewCourse course
   String username
   Instant updatedAt
   String errorMessage
   Instant createdAt
}
class FailureRecordBuilder {
  ~ FailureRecordBuilder() 
  + userPassword(String) FailureRecordBuilder
  + build() FailureRecord
  + id(Long) FailureRecordBuilder
  + sagaFailedStep(SagaFailedStep) FailureRecordBuilder
  + createdAt(Instant) FailureRecordBuilder
  + course(NewCourse) FailureRecordBuilder
  + updatedAt(Instant) FailureRecordBuilder
  + username(String) FailureRecordBuilder
  + toString() String
  + errorMessage(String) FailureRecordBuilder
}
class FailureRecordRepository {
<<Interface>>
  + findAllBySagaFailedStep(SagaFailedStep) List~FailureRecord~
}
class FieldNotSpecifiedException {
  + FieldNotSpecifiedException(String) 
}
class FileUploadFailedEvent {
  + FileUploadFailedEvent(User, NewCourse, Exception) 
  - User user
  - NewCourse course
  - Exception exception
  + equals(Object) boolean
  + toString() String
  + hashCode() int
  # canEqual(Object) boolean
   Exception exception
   User user
   NewCourse course
}
class FileUploadedEvent {
  + FileUploadedEvent(User, NewCourse, File) 
  - User user
  - NewCourse course
  - File pdf
  + hashCode() int
  # canEqual(Object) boolean
  + equals(Object) boolean
  + toString() String
   User user
   File pdf
   NewCourse course
}
class GetCoursesDelegate {
  + GetCoursesDelegate(NewCourseService, UserService) 
  + execute(DelegateExecution) void
}
class GetExerciseDelegate {
  + GetExerciseDelegate(NewModuleService) 
  + execute(DelegateExecution) void
}
class GetModulesDelegate {
  + GetModulesDelegate(UserService, NewCourseService) 
  + execute(DelegateExecution) void
}
class InvalidFieldException {
  + InvalidFieldException(String) 
}
class JwtAuthenticationFilter {
  + JwtAuthenticationFilter(JwtService, UserService) 
  # doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain) void
}
class JwtAuthenticationResponse {
  + JwtAuthenticationResponse() 
  + JwtAuthenticationResponse(String) 
  - String token
  # canEqual(Object) boolean
  + hashCode() int
  + equals(Object) boolean
  + toString() String
   String token
}
class JwtService {
<<Interface>>
  + generateToken(UserDetails) String
  + isTokenValid(String, UserDetails) boolean
  + extractUserName(String) String
}
class JwtServiceImpl {
  + JwtServiceImpl() 
  - isTokenExpired(String) boolean
  + extractUserName(String) String
  - generateToken(Map~String, Object~, UserDetails) String
  - extractAllClaims(String) Claims
  + isTokenValid(String, UserDetails) boolean
  - extractExpiration(String) Date
  - extractClaim(String, Function~Claims, T~) T
  + generateToken(UserDetails) String
   SecretKey signingKey
}
class JwtTokenExpiredException {
  + JwtTokenExpiredException(String) 
}
class KafkaListener {
  + KafkaListener(RegisterService) 
  + handle(MiniUser) void
}
class KafkaProducerConfig {
  + KafkaProducerConfig() 
  + producerFactory() ProducerFactory~String, KafkaUser~
  + kafkaTemplate() KafkaTemplate~String, KafkaUser~
}
class KafkaUser {
  + KafkaUser(String, String, String) 
  + KafkaUser() 
  - String email
  - String username
  - String password
  + hashCode() int
  + toString() String
  # canEqual(Object) boolean
  + equals(Object) boolean
   String email
   String password
   String username
}
class LearningPlatform {
  + LearningPlatform() 
  # configure(SpringApplicationBuilder) SpringApplicationBuilder
  + main(String[]) void
}
class LoginRequest {
  + LoginRequest() 
  - String email
  - String password
  + equals(Object) boolean
  # canEqual(Object) boolean
  + hashCode() int
  + toString() String
   String email
   String password
}
class MailCreationException {
  + MailCreationException(String) 
}
class MailSendingException {
  + MailSendingException(String) 
}
class MessageProducer {
  + MessageProducer(KafkaTemplate~String, KafkaUser~) 
  + sendMessage(String, KafkaUser) void
}
class MiniUser {
  + MiniUser(String, String, String) 
  + MiniUser() 
  - String password
  - String username
  - String email
  # canEqual(Object) boolean
  + hashCode() int
  + equals(Object) boolean
  + builder() MiniUserBuilder
  + toString() String
   String email
   String password
   String username
}
class MiniUserBuilder {
  ~ MiniUserBuilder() 
  + toString() String
  + email(String) MiniUserBuilder
  + build() MiniUser
  + username(String) MiniUserBuilder
  + password(String) MiniUserBuilder
}
class MinioServiceImpl {
  + MinioServiceImpl() 
  + uploadFile(String, String, File) void
  - getNewFileName(String, String) String
  - ensureBucketExists() void
  + reinitMinio() void
}
class ModuleController {
  + ModuleController(NewModuleService) 
  + completeModule(UUID) ResponseEntity~Map~String, Object~~
  + getModuleById(UUID) ResponseEntity~Map~String, Object~~
  + getAllModules(Long) ResponseEntity~Map~String, Object~~
}
class ModuleController {
  + ModuleController(NewModuleService) 
  + getSpecificModule(UUID) ResponseEntity~Map~String, Object~~
  + linkExerciseToModule(UUID, UUID) ResponseEntity~Map~String, Object~~
  + createModule(NewModuleDto) ResponseEntity~Map~String, Object~~
  + updateModule(UUID, NewModuleDto) ResponseEntity~Map~String, Object~~
  + deleteModule(UUID) ResponseEntity~Map~String, Object~~
   ResponseEntity~Map~String, Object~~ allCourses
}
class NewCourse {
  + NewCourse(UUID, String, String, BigDecimal, Topic, LocalDateTime, List~NewModule~, List~NewCourse~) 
  + NewCourse() 
  - String name
  - List~NewCourse~ additionalCourseList
  - BigDecimal price
  - UUID uuid
  - Topic topic
  - String description
  - List~NewModule~ newModuleList
  - LocalDateTime creationTime
  # canEqual(Object) boolean
  + builder() NewCourseBuilder
  + preRemove() void
  + equals(Object) boolean
  + prePersist() void
  + hashCode() int
  + toString() String
   List~NewCourse~ additionalCourseList
   String name
   String description
   List~NewModule~ newModuleList
   Topic topic
   LocalDateTime creationTime
   BigDecimal price
   UUID uuid
}
class NewCourseBuilder {
  ~ NewCourseBuilder() 
  + newModuleList(List~NewModule~) NewCourseBuilder
  + topic(Topic) NewCourseBuilder
  + build() NewCourse
  + toString() String
  + name(String) NewCourseBuilder
  + price(BigDecimal) NewCourseBuilder
  + creationTime(LocalDateTime) NewCourseBuilder
  + description(String) NewCourseBuilder
  + uuid(UUID) NewCourseBuilder
  + additionalCourseList(List~NewCourse~) NewCourseBuilder
}
class NewCourseDto {
  + NewCourseDto(UUID, String, String, BigDecimal, Topic, LocalDateTime, List~NewModuleDto~, List~NewCourseDto~) 
  + NewCourseDto() 
  - Topic topic
  - LocalDateTime creationTime
  - String name
  - BigDecimal price
  - String description
  - UUID uuid
  - List~NewCourseDto~ additionalCourseList
  - List~NewModuleDto~ newModuleList
  + toString() String
  + builder() NewCourseDtoBuilder
  + hashCode() int
  # canEqual(Object) boolean
  + equals(Object) boolean
   List~NewCourseDto~ additionalCourseList
   String name
   String description
   List~NewModuleDto~ newModuleList
   Topic topic
   LocalDateTime creationTime
   BigDecimal price
   UUID uuid
}
class NewCourseDtoBuilder {
  ~ NewCourseDtoBuilder() 
  + name(String) NewCourseDtoBuilder
  + topic(Topic) NewCourseDtoBuilder
  + creationTime(LocalDateTime) NewCourseDtoBuilder
  + additionalCourseList(List~NewCourseDto~) NewCourseDtoBuilder
  + description(String) NewCourseDtoBuilder
  + build() NewCourseDto
  + toString() String
  + uuid(UUID) NewCourseDtoBuilder
  + price(BigDecimal) NewCourseDtoBuilder
  + newModuleList(List~NewModuleDto~) NewCourseDtoBuilder
}
class NewCourseMapper {
  + NewCourseMapper() 
  + toDto(NewCourse) NewCourseDto
  + toEntity(NewCourseDto) NewCourse
}
class NewCourseRepository {
<<Interface>>
  + removeByNewModuleList(List~NewModule~) void
}
class NewCourseService {
<<Interface>>
  + addAll(List~NewCourse~) List~NewCourse~
  + getCourseByUserID(Long) List~NewCourse~
  + deleteCourse(UUID) void
  + addAdditionalCourses(UUID, UUID) NewCourse
  + isCourseFinished(User, UUID) Boolean
  + find(UUID) NewCourse
  + getCourseByUUID(UUID) NewCourse
  + enrollStudent(Long, UUID) List~NewCourse~
  + updateCourse(UUID, NewCourseDto) NewCourse
  + createCourse(NewCourseDto) NewCourse
  + linkModule(UUID, UUID) NewCourse
   List~NewCourse~ allCourses
}
class NewCourseServiceImpl {
  + NewCourseServiceImpl(PlatformTransactionManager, NewCourseRepository, ApplicationRepository, NewModuleRepository, StudentRepository, UserService) 
  + addAll(List~NewCourse~) List~NewCourse~
  - isModuleFinished(User, UUID) Boolean
  + getCourseByUserID(Long) List~NewCourse~
  + addAdditionalCourses(UUID, UUID) NewCourse
  + enrollStudent(Long, UUID) List~NewCourse~
  + linkModule(UUID, UUID) NewCourse
  - validateRequiredFieldsOrThrow(NewCourseDto) void
  + createCourse(NewCourseDto) NewCourse
  + updateCourse(UUID, NewCourseDto) NewCourse
  + isCourseFinished(User, UUID) Boolean
  + deleteCourse(UUID) void
  + getCourseByUUID(UUID) NewCourse
  + find(UUID) NewCourse
   User currentUser
   List~NewCourse~ allCourses
}
class NewExercise {
  + NewExercise(UUID, String, String, String, Integer, LocalDateTime) 
  + NewExercise() 
  - String description
  - UUID uuid
  - String name
  - Integer points
  - LocalDateTime createdAt
  - String answer
  + equals(Object) boolean
  + builder() NewExerciseBuilder
  # canEqual(Object) boolean
  + prePersist() void
  + hashCode() int
  + toString() String
   String name
   String description
   LocalDateTime createdAt
   String answer
   Integer points
   UUID uuid
}
class NewExerciseBuilder {
  ~ NewExerciseBuilder() 
  + description(String) NewExerciseBuilder
  + uuid(UUID) NewExerciseBuilder
  + points(Integer) NewExerciseBuilder
  + name(String) NewExerciseBuilder
  + answer(String) NewExerciseBuilder
  + toString() String
  + createdAt(LocalDateTime) NewExerciseBuilder
  + build() NewExercise
}
class NewExerciseDto {
  + NewExerciseDto() 
  + NewExerciseDto(UUID, String, String, String, Integer) 
  - UUID uuid
  - String description
  - Integer points
  - String answer
  - String name
  + hashCode() int
  + equals(Object) boolean
  + toString() String
  # canEqual(Object) boolean
  + builder() NewExerciseDtoBuilder
   String name
   String description
   String answer
   Integer points
   UUID uuid
}
class NewExerciseDtoBuilder {
  ~ NewExerciseDtoBuilder() 
  + toString() String
  + name(String) NewExerciseDtoBuilder
  + points(Integer) NewExerciseDtoBuilder
  + uuid(UUID) NewExerciseDtoBuilder
  + description(String) NewExerciseDtoBuilder
  + answer(String) NewExerciseDtoBuilder
  + build() NewExerciseDto
}
class NewExerciseMapper {
  + NewExerciseMapper() 
  + toEntity(NewExerciseDto) NewExercise
  + toDto(NewExercise) NewExerciseDto
}
class NewExerciseRepository {
<<Interface>>

}
class NewExerciseService {
<<Interface>>
  + find(UUID) NewExercise
  + updateNewExercise(UUID, NewExerciseDto) NewExercise
  + deleteNewExercise(UUID) void
  + submitAnswer(UUID, String) Boolean
  + createNewExercise(NewExerciseDto) NewExercise
   List~NewExercise~ allExercises
}
class NewExerciseServiceImpl {
  + NewExerciseServiceImpl(NewExerciseRepository, PlatformTransactionManager, NewModuleRepository, StudentRepository, AuthService) 
  + updateNewExercise(UUID, NewExerciseDto) NewExercise
  + submitAnswer(UUID, String) Boolean
  + find(UUID) NewExercise
  + createNewExercise(NewExerciseDto) NewExercise
  + deleteNewExercise(UUID) void
   List~NewExercise~ allExercises
}
class NewModule {
  + NewModule() 
  + NewModule(UUID, String, String, LocalDateTime, List~NewExercise~) 
  - List~NewExercise~ exercises
  - String description
  - LocalDateTime createdAt
  - String name
  - UUID uuid
  + prePersist() void
  + hashCode() int
  + equals(Object) boolean
  + toString() String
  + builder() NewModuleBuilder
  # canEqual(Object) boolean
   String name
   String description
   LocalDateTime createdAt
   List~NewExercise~ exercises
   UUID uuid
}
class NewModuleBuilder {
  ~ NewModuleBuilder() 
  + build() NewModule
  + name(String) NewModuleBuilder
  + createdAt(LocalDateTime) NewModuleBuilder
  + description(String) NewModuleBuilder
  + exercises(List~NewExercise~) NewModuleBuilder
  + toString() String
  + uuid(UUID) NewModuleBuilder
}
class NewModuleDto {
  + NewModuleDto() 
  + NewModuleDto(UUID, String, String, List~NewExerciseDto~) 
  - UUID uuid
  - String description
  - String name
  - List~NewExerciseDto~ exercises
  # canEqual(Object) boolean
  + toString() String
  + hashCode() int
  + equals(Object) boolean
  + builder() NewModuleDtoBuilder
   String name
   String description
   List~NewExerciseDto~ exercises
   UUID uuid
}
class NewModuleDtoBuilder {
  ~ NewModuleDtoBuilder() 
  + name(String) NewModuleDtoBuilder
  + description(String) NewModuleDtoBuilder
  + exercises(List~NewExerciseDto~) NewModuleDtoBuilder
  + build() NewModuleDto
  + uuid(UUID) NewModuleDtoBuilder
  + toString() String
}
class NewModuleMapper {
  + NewModuleMapper() 
  + toEntity(NewModuleDto) NewModule
  + toDto(NewModule) NewModuleDto
}
class NewModuleRepository {
<<Interface>>
  + removeByExercises(List~NewExercise~) void
}
class NewModuleService {
<<Interface>>
  + getAllModules(Long) List~NewModule~
  + isModuleComplete(UUID) Boolean
  + getModuleByUUID(UUID) NewModule
  + createModule(NewModuleDto) NewModule
  + deleteModule(UUID) void
  + linkExercise(UUID, UUID) NewModule
  + updateModule(UUID, NewModuleDto) NewModule
   List~NewModule~ allModules
}
class NewModuleServiceImpl {
  + NewModuleServiceImpl(NewModuleRepository, NewExerciseRepository, PlatformTransactionManager, NewCourseRepository, AuthService, StudentRepository) 
  + linkExercise(UUID, UUID) NewModule
  + getAllModules(Long) List~NewModule~
  + isModuleComplete(UUID) Boolean
  + createModule(NewModuleDto) NewModule
  + getModuleByUUID(UUID) NewModule
  + deleteModule(UUID) void
  + updateModule(UUID, NewModuleDto) NewModule
   List~NewModule~ allModules
}
class NotExistException {
  + NotExistException(String) 
}
class ObjectAlreadyExistException {
  + ObjectAlreadyExistException(String) 
}
class ObjectNotExistException {
  + ObjectNotExistException(String) 
}
class ObjectNotFoundException {
  + ObjectNotFoundException(String) 
}
class OpenApiConfig {
  + OpenApiConfig() 
}
class ProcessApplicationAnswerDelegate {
  + ProcessApplicationAnswerDelegate(ApplicationService) 
  + execute(DelegateExecution) void
}
class ProcessingDelegate {
  + ProcessingDelegate() 
  + execute(DelegateExecution) void
}
class RecoverAllDelegate {
  + RecoverAllDelegate(UserFailureService) 
  + execute(DelegateExecution) void
}
class RecoverSageDelegate {
  + RecoverSageDelegate(RecoveryService) 
  + execute(DelegateExecution) void
}
class RecoveryService {
  + RecoveryService(FailureRecordRepository, ApplicationEventPublisher, CertificateGenerator, SimpleStorageServiceWithRetry) 
  + recoverFileUploads() void
}
class RegisterService {
<<Interface>>
  + register(MiniUser) Boolean
}
class RegisterServiceImpl {
  + RegisterServiceImpl(UserFailureRepository) 
  + register(MiniUser) Boolean
}
class RegistrationDelegate {
  + RegistrationDelegate(IdentityService, AuthService) 
  + execute(DelegateExecution) void
}
class RegistrationPreparingDelegate {
  + RegistrationPreparingDelegate(NewCourseService) 
  + execute(DelegateExecution) void
}
class RegistrationRequestDto {
  + RegistrationRequestDto() 
  + RegistrationRequestDto(String, String, String, String, String, String) 
  - String password
  - String firstName
  - String lastName
  - String email
  - String phoneNumber
  - String userID
  + toString() String
  + equals(Object) boolean
  + hashCode() int
  # canEqual(Object) boolean
  + builder() RegistrationRequestDtoBuilder
   String password
   String firstName
   String lastName
   String email
   String phoneNumber
   String userID
}
class RegistrationRequestDtoBuilder {
  ~ RegistrationRequestDtoBuilder() 
  + firstName(String) RegistrationRequestDtoBuilder
  + phoneNumber(String) RegistrationRequestDtoBuilder
  + email(String) RegistrationRequestDtoBuilder
  + password(String) RegistrationRequestDtoBuilder
  + build() RegistrationRequestDto
  + lastName(String) RegistrationRequestDtoBuilder
  + userID(String) RegistrationRequestDtoBuilder
  + toString() String
}
class RequestApplicationDelegate {
  + RequestApplicationDelegate(UserService, ApplicationService) 
  + execute(DelegateExecution) void
}
class RestAdviser {
  + RestAdviser() 
  + handleAuthorizationDeniedException(AuthorizationDeniedException) ExceptionWrapper
  + handleFieldNotSpecifiedException(RuntimeException) ExceptionWrapper
  + UsernameNotFoundException(UsernameNotFoundException) ExceptionWrapper
  + handleObjectNotFoundException(ObjectNotFoundException) ExceptionWrapper
  + noHandlerFoundException(Exception) ExceptionWrapper
  + handleObjectNotExistException(ObjectNotExistException) ExceptionWrapper
  + AuthorizeException(RuntimeException) ExceptionWrapper
  + handleRuntimeException(RuntimeException) ExceptionWrapper
  + courseNotExistException(Exception) ExceptionWrapper
  + handleObjectAlreadyExistException(ObjectAlreadyExistException) ExceptionWrapper
}
class Role {
<<enumeration>>
  - Role() 
  + values() Role[]
  + valueOf(String) Role
}
class RoleAssignDelegate {
  + RoleAssignDelegate(AdminPanelService) 
  + execute(DelegateExecution) void
}
class S3Exception {
  + S3Exception() 
  + S3Exception(String) 
}
class SagaFailedStep {
<<enumeration>>
  - SagaFailedStep() 
  + values() SagaFailedStep[]
  + valueOf(String) SagaFailedStep
}
class SagaListeners {
  + SagaListeners(CertificateGenerator, SimpleStorageServiceWithRetry, MessageProducer, EmailService, FailureRecordRepository, ApplicationEventPublisher) 
  + handle(CertificateSentEvent) void
  - saveFail(String, String, NewCourse, String, SagaFailedStep) void
  + handle(FileUploadedEvent) void
  + handle(CertificateGenerationFailedEvent) void
  + handle(FileUploadFailedEvent) void
  + handle(CourseCompletedEvent) void
  + handle(CertificateGeneratedEvent) void
}
class SecurityConfig {
  + SecurityConfig(JwtAuthenticationFilter, UserService) 
  + authenticationProvider() AuthenticationProvider
  + passwordEncoder() PasswordEncoder
  + securityFilterChain(HttpSecurity) SecurityFilterChain
  + authenticationManager(AuthenticationConfiguration) AuthenticationManager
}
class SetupDelegate {
  + SetupDelegate() 
  + execute(DelegateExecution) void
}
class SimpleStorageService {
<<Interface>>
  + uploadFile(String, String, File) void
}
class SimpleStorageServiceWithRetry {
  + SimpleStorageServiceWithRetry(SimpleStorageService) 
  + uploadWithRetry(String, String, File) void
}
class SimpleStorageStubImpl {
  + SimpleStorageStubImpl() 
  + uploadFile(String, String, File) void
}
class SpiResponse {
  + SpiResponse() 
  - String message
  - String success
  + equals(Object) boolean
  + hashCode() int
  + toString() String
  # canEqual(Object) boolean
   String message
   String success
}
class StartSendingCertificateDelegate {
  + StartSendingCertificateDelegate(UserService, NewCourseService, ApplicationEventPublisher) 
  + execute(DelegateExecution) void
}
class Student {
  + Student(Long, List~NewExercise~, User, List~NewCourse~) 
  + Student() 
  - Long id
  - List~NewExercise~ finishedExercises
  - List~NewCourse~ courses
  - User user
  + hashCode() int
  + builder() StudentBuilder
  # canEqual(Object) boolean
  + equals(Object) boolean
  + toString() String
   List~NewCourse~ courses
   List~NewExercise~ finishedExercises
   Long id
   User user
}
class StudentBuilder {
  ~ StudentBuilder() 
  + id(Long) StudentBuilder
  + user(User) StudentBuilder
  + toString() String
  + build() Student
  + courses(List~NewCourse~) StudentBuilder
  + finishedExercises(List~NewExercise~) StudentBuilder
}
class StudentRepository {
<<Interface>>
  + findByUser_Id(Long) Optional~Student~
}
class Topic {
<<enumeration>>
  - Topic() 
  + values() Topic[]
  + valueOf(String) Topic
}
class User {
  + User(Long, String, String, String, String, String, String, Role) 
  + User() 
  - String camundaUserID
  - Long id
  - String password
  - String firstName
  - String lastName
  - Role role
  - String email
  - String phoneNumber
  + equals(Object) boolean
  # canEqual(Object) boolean
  + toString() String
  + builder() UserBuilder
  + hashCode() int
   String password
   Role role
   String lastName
   Long id
   String email
   String phoneNumber
   Collection~GrantedAuthority~ authorities
   String firstName
   String camundaUserID
   String username
}
class UserBuilder {
  ~ UserBuilder() 
  + phoneNumber(String) UserBuilder
  + camundaUserID(String) UserBuilder
  + firstName(String) UserBuilder
  + lastName(String) UserBuilder
  + role(Role) UserBuilder
  + id(Long) UserBuilder
  + password(String) UserBuilder
  + build() User
  + email(String) UserBuilder
  + toString() String
}
class UserDatabase {
<<Interface>>
  + findByEmail(String) Optional~UserXml~
  + findById(Long) Optional~UserXml~
  + truncate() void
  + save(UserXml) UserXml
}
class UserDto {
  + UserDto() 
  + UserDto(String, String, String, String, String, Role) 
  - String email
  - String phoneNumber
  - String firstname
  - Role role
  - String password
  - String lastname
  + equals(Object) boolean
  # canEqual(Object) boolean
  + builder() UserDtoBuilder
  + hashCode() int
  + toString() String
   String firstname
   String password
   Role role
   String email
   String lastname
   String phoneNumber
}
class UserDtoBuilder {
  ~ UserDtoBuilder() 
  + phoneNumber(String) UserDtoBuilder
  + toString() String
  + firstname(String) UserDtoBuilder
  + email(String) UserDtoBuilder
  + role(Role) UserDtoBuilder
  + lastname(String) UserDtoBuilder
  + password(String) UserDtoBuilder
  + build() UserDto
}
class UserEnrollmentService {
<<Interface>>
  + processEnrolment(Long, String) void
}
class UserEnrollmentServiceImpl {
  + UserEnrollmentServiceImpl(PlatformTransactionManager, EmailService, NewCourseService, AuthService, UserService, ApplicationServiceImpl) 
  + processEnrolment(Long, String) void
}
class UserFailure {
  + UserFailure() 
  - String username
  - String email
  - String password
  - Boolean isFailed
  + toString() String
  + equals(Object) boolean
  + hashCode() int
  # canEqual(Object) boolean
   Boolean isFailed
   String password
   String username
   String email
}
class UserFailureRepository {
<<Interface>>
  + findAllByIsFailed(Boolean) List~UserFailure~
}
class UserFailureService {
<<Interface>>
  + saveFailedUser(UserFailure) void
  + recoverAll() void
}
class UserFailureServiceImpl {
  + UserFailureServiceImpl(UserFailureRepository, RegisterService) 
  + saveFailedUser(UserFailure) void
  + recoverAll() void
}
class UserRepository {
<<Interface>>
  + findByEmail(String) Optional~User~
}
class UserService {
<<Interface>>
  + getUserByEmail(String) User
  + updateUser(User) User
  + isExist(String) boolean
  + enrollUser(User, NewCourse) void
  + add(User) User
   UserDetailsService userDetailsService
}
class UserServiceImpl {
  + UserServiceImpl(UserRepository, PlatformTransactionManager) 
  + isExist(String) boolean
  + add(User) User
  + updateUser(User) User
  + getUserByEmail(String) User
  + enrollUser(User, NewCourse) void
   UserDetailsService userDetailsService
}
class UserXml {
  + UserXml() 
  + UserXml(Long, String, String, String, String, String, Role) 
  - String firstName
  - String username
  - String lastName
  - String password
  - String phoneNumber
  - Long id
  - Role role
  # canEqual(Object) boolean
  + equals(Object) boolean
  + toString() String
  + hashCode() int
  + builder() UserXmlBuilder
   String password
   Role role
   String lastName
   Long id
   String phoneNumber
   Collection~GrantedAuthority~ authorities
   String firstName
   String username
}
class UserXmlBuilder {
  ~ UserXmlBuilder() 
  + username(String) UserXmlBuilder
  + build() UserXml
  + toString() String
  + lastName(String) UserXmlBuilder
  + id(Long) UserXmlBuilder
  + firstName(String) UserXmlBuilder
  + phoneNumber(String) UserXmlBuilder
  + password(String) UserXmlBuilder
  + role(Role) UserXmlBuilder
}
class UserXmlRepository {
  + UserXmlRepository() 
  + save(UserXml) UserXml
  + findById(Long) Optional~UserXml~
  + init() void
  + truncate() void
  + findByEmail(String) Optional~UserXml~
   UsersXmlWrapper userXmlWrapper
}
class UsersXmlWrapper {
  + UsersXmlWrapper() 
  + UsersXmlWrapper(List~UserXml~) 
  - List~UserXml~ users
  + hashCode() int
  # canEqual(Object) boolean
  + toString() String
  + equals(Object) boolean
   List~UserXml~ users
}
class ValidateAndCreateCourseDelegate {
  + ValidateAndCreateCourseDelegate(NewCourseService) 
  + execute(DelegateExecution) void
}
class ValidateAndCreateExerciseDelegate {
  + ValidateAndCreateExerciseDelegate(NewExerciseService) 
  + execute(DelegateExecution) void
}
class ValidateAndCreateModuleDelegate {
  + ValidateAndCreateModuleDelegate(NewModuleService) 
  + execute(DelegateExecution) void
}
class ValidateExerciseDelegate {
  + ValidateExerciseDelegate(NewExerciseService, StudentRepository, UserService) 
  + execute(DelegateExecution) void
}
class ValidateFinishingCourseDelegate {
  + ValidateFinishingCourseDelegate(NewCourseService, UserService) 
  + execute(DelegateExecution) void
}
class ValidateUserRoleDelegate {
  + ValidateUserRoleDelegate(UserService) 
  + execute(DelegateExecution) void
}
class WebMvcConfiguration {
  ~ WebMvcConfiguration() 
  + configureContentNegotiation(ContentNegotiationConfigurer) void
}

AdminPanelController "1" *--> "adminPanelService 1" AdminPanelServiceImpl 
AdminPanelServiceImpl  ..>  AdminPanelService 
AdminPanelServiceImpl "1" *--> "userService 1" UserService 
Application "1" *--> "status 1" ApplicationStatus 
Application "1" *--> "newCourse 1" NewCourse 
Application "1" *--> "user 1" User 
Application  -->  ApplicationBuilder 
ApplicationBuilder "1" *--> "status 1" ApplicationStatus 
ApplicationBuilder "1" *--> "newCourse 1" NewCourse 
ApplicationBuilder "1" *--> "user 1" User 
ApplicationController "1" *--> "applicationService 1" ApplicationServiceImpl 
ApplicationController "1" *--> "userEnrollmentService 1" UserEnrollmentServiceImpl 
ApplicationResponseDto "1" *--> "jwt 1" JwtAuthenticationResponse 
ApplicationResponseDto  -->  ApplicationResponseDtoBuilder 
ApplicationResponseDtoBuilder "1" *--> "jwt 1" JwtAuthenticationResponse 
ApplicationServiceImpl "1" *--> "repository 1" ApplicationRepository 
ApplicationServiceImpl  ..>  ApplicationService 
ApplicationServiceImpl "1" *--> "courseService 1" NewCourseService 
ApplicationServiceImpl "1" *--> "userService 1" UserService 
AuthController "1" *--> "authService 1" AuthService 
AuthServiceImpl "1" *--> "applicationService 1" ApplicationService 
AuthServiceImpl  ..>  AuthService 
AuthServiceImpl "1" *--> "jwtService 1" JwtService 
AuthServiceImpl "1" *--> "courseService 1" NewCourseService 
AuthServiceImpl "1" *--> "studentRepository 1" StudentRepository 
AuthServiceImpl "1" *--> "userService 1" UserService 
Bootstrap "1" *--> "applicationService 1" ApplicationService 
Bootstrap "1" *--> "authService 1" AuthService 
Bootstrap "1" *--> "newCourseService 1" NewCourseService 
Bootstrap "1" *--> "newExerciseService 1" NewExerciseService 
Bootstrap "1" *--> "newModuleService 1" NewModuleService 
Bootstrap "1" *--> "userService 1" UserService 
CasualAuth "1" *--> "userService 1" UserService 
CertificateController "1" *--> "authService 1" AuthService 
CertificateController "1" *--> "certificateManagerService 1" CertificateManager 
CertificateGeneratedEvent "1" *--> "course 1" NewCourse 
CertificateGeneratedEvent "1" *--> "user 1" User 
CertificateGenerationFailedEvent "1" *--> "course 1" NewCourse 
CertificateGenerationFailedEvent "1" *--> "user 1" User 
CertificateManagerImpl  ..>  CertificateManager 
CertificateManagerImpl "1" *--> "courseService 1" NewCourseService 
CertificatePdfGenerator  ..>  CertificateGenerator 
CertificateSentEvent "1" *--> "user 1" User 
CompanyDto  -->  CompanyDtoBuilder 
CourseCompletedEvent "1" *--> "course 1" NewCourse 
CourseCompletedEvent "1" *--> "user 1" User 
CourseController "1" *--> "authService 1" AuthService 
CourseController "1" *--> "newCourseService 1" NewCourseService 
CourseController "1" *--> "courseService 1" NewCourseService 
EmailServiceImpl  ..>  EmailService 
EmailServiceStub  ..>  EmailService 
EnrollmentController "1" *--> "courseService 1" NewCourseService 
ExerciseController "1" *--> "newExerciseService 1" NewExerciseService 
ExerciseController "1" *--> "exerciseService 1" NewExerciseService 
FailureRecord "1" *--> "course 1" NewCourse 
FailureRecord "1" *--> "sagaFailedStep 1" SagaFailedStep 
FailureRecord  -->  FailureRecordBuilder 
FailureRecordBuilder "1" *--> "course 1" NewCourse 
FailureRecordBuilder "1" *--> "sagaFailedStep 1" SagaFailedStep 
FileUploadFailedEvent "1" *--> "course 1" NewCourse 
FileUploadFailedEvent "1" *--> "user 1" User 
FileUploadedEvent "1" *--> "course 1" NewCourse 
FileUploadedEvent "1" *--> "user 1" User 
GetCoursesDelegate "1" *--> "newCourseService 1" NewCourseService 
GetCoursesDelegate "1" *--> "userService 1" UserService 
GetExerciseDelegate "1" *--> "newModuleService 1" NewModuleService 
GetModulesDelegate "1" *--> "newCourseService 1" NewCourseService 
GetModulesDelegate "1" *--> "userService 1" UserService 
JwtAuthenticationFilter "1" *--> "jwtService 1" JwtService 
JwtAuthenticationFilter "1" *--> "userService 1" UserService 
JwtServiceImpl  ..>  JwtService 
KafkaListener "1" *--> "registerService 1" RegisterService 
MiniUser  -->  MiniUserBuilder 
MinioServiceImpl  ..>  SimpleStorageService 
ModuleController "1" *--> "newModuleService 1" NewModuleService 
ModuleController "1" *--> "moduleService 1" NewModuleService 
NewCourse "1" *--> "newModuleList *" NewModule 
NewCourse "1" *--> "topic 1" Topic 
NewCourse  -->  NewCourseBuilder 
NewCourseBuilder "1" *--> "additionalCourseList *" NewCourse 
NewCourseBuilder "1" *--> "newModuleList *" NewModule 
NewCourseBuilder "1" *--> "topic 1" Topic 
NewCourseDto "1" *--> "newModuleList *" NewModuleDto 
NewCourseDto "1" *--> "topic 1" Topic 
NewCourseDto  -->  NewCourseDtoBuilder 
NewCourseDtoBuilder "1" *--> "additionalCourseList *" NewCourseDto 
NewCourseDtoBuilder "1" *--> "newModuleList *" NewModuleDto 
NewCourseDtoBuilder "1" *--> "topic 1" Topic 
NewCourseServiceImpl "1" *--> "applicationRepository 1" ApplicationRepository 
NewCourseServiceImpl "1" *--> "newCourseRepository 1" NewCourseRepository 
NewCourseServiceImpl  ..>  NewCourseService 
NewCourseServiceImpl "1" *--> "newModuleRepository 1" NewModuleRepository 
NewCourseServiceImpl "1" *--> "studentRepository 1" StudentRepository 
NewCourseServiceImpl "1" *--> "userService 1" UserService 
NewExercise  -->  NewExerciseBuilder 
NewExerciseDto  -->  NewExerciseDtoBuilder 
NewExerciseServiceImpl "1" *--> "authService 1" AuthService 
NewExerciseServiceImpl "1" *--> "newExerciseRepository 1" NewExerciseRepository 
NewExerciseServiceImpl  ..>  NewExerciseService 
NewExerciseServiceImpl "1" *--> "newModuleRepository 1" NewModuleRepository 
NewExerciseServiceImpl "1" *--> "studentRepository 1" StudentRepository 
NewModule "1" *--> "exercises *" NewExercise 
NewModuleBuilder "1" *--> "exercises *" NewExercise 
NewModule  -->  NewModuleBuilder 
NewModuleDto "1" *--> "exercises *" NewExerciseDto 
NewModuleDtoBuilder "1" *--> "exercises *" NewExerciseDto 
NewModuleDto  -->  NewModuleDtoBuilder 
NewModuleServiceImpl "1" *--> "authService 1" AuthService 
NewModuleServiceImpl "1" *--> "newCourseRepository 1" NewCourseRepository 
NewModuleServiceImpl "1" *--> "newExerciseRepository 1" NewExerciseRepository 
NewModuleServiceImpl "1" *--> "newModuleRepository 1" NewModuleRepository 
NewModuleServiceImpl  ..>  NewModuleService 
NewModuleServiceImpl "1" *--> "studentRepository 1" StudentRepository 
ProcessApplicationAnswerDelegate "1" *--> "applicationService 1" ApplicationService 
RecoverAllDelegate "1" *--> "userFailureService 1" UserFailureService 
RecoverSageDelegate "1" *--> "recoveryService 1" RecoveryService 
RecoveryService "1" *--> "certificateGenerator 1" CertificateGenerator 
RecoveryService "1" *--> "failureRepo 1" FailureRecordRepository 
RecoveryService "1" *--> "simpleStorageServiceWithRetry 1" SimpleStorageServiceWithRetry 
RegisterServiceImpl  ..>  RegisterService 
RegisterServiceImpl "1" *--> "userFailureRepository 1" UserFailureRepository 
RegistrationDelegate "1" *--> "authService 1" AuthService 
RegistrationPreparingDelegate "1" *--> "newCourseService 1" NewCourseService 
RegistrationRequestDto  -->  RegistrationRequestDtoBuilder 
RequestApplicationDelegate "1" *--> "applicationService 1" ApplicationService 
RequestApplicationDelegate "1" *--> "userService 1" UserService 
RoleAssignDelegate "1" *--> "adminPanelService 1" AdminPanelService 
SagaListeners "1" *--> "certificateGenerator 1" CertificateGenerator 
SagaListeners "1" *--> "emailService 1" EmailService 
SagaListeners "1" *--> "failureRecordRepository 1" FailureRecordRepository 
SagaListeners "1" *--> "messageProducer 1" MessageProducer 
SagaListeners "1" *--> "simpleStorageServiceWithRetry 1" SimpleStorageServiceWithRetry 
SecurityConfig "1" *--> "jwtAuthenticationFilter 1" JwtAuthenticationFilter 
SecurityConfig "1" *--> "userService 1" UserService 
SimpleStorageServiceWithRetry "1" *--> "delegate 1" SimpleStorageService 
SimpleStorageStubImpl  ..>  SimpleStorageService 
StartSendingCertificateDelegate "1" *--> "newCourseService 1" NewCourseService 
StartSendingCertificateDelegate "1" *--> "userService 1" UserService 
Student "1" *--> "courses *" NewCourse 
Student "1" *--> "finishedExercises *" NewExercise 
Student "1" *--> "user 1" User 
StudentBuilder "1" *--> "courses *" NewCourse 
StudentBuilder "1" *--> "finishedExercises *" NewExercise 
Student  -->  StudentBuilder 
StudentBuilder "1" *--> "user 1" User 
User "1" *--> "role 1" Role 
UserBuilder "1" *--> "role 1" Role 
User  -->  UserBuilder 
UserDto "1" *--> "role 1" Role 
UserDtoBuilder "1" *--> "role 1" Role 
UserDto  -->  UserDtoBuilder 
UserEnrollmentServiceImpl "1" *--> "applicationService 1" ApplicationServiceImpl 
UserEnrollmentServiceImpl "1" *--> "authService 1" AuthService 
UserEnrollmentServiceImpl "1" *--> "emailService 1" EmailService 
UserEnrollmentServiceImpl "1" *--> "courseService 1" NewCourseService 
UserEnrollmentServiceImpl  ..>  UserEnrollmentService 
UserEnrollmentServiceImpl "1" *--> "userService 1" UserService 
UserFailureServiceImpl "1" *--> "registerService 1" RegisterService 
UserFailureServiceImpl "1" *--> "userFailureRepository 1" UserFailureRepository 
UserFailureServiceImpl  ..>  UserFailureService 
UserServiceImpl "1" *--> "userRepository 1" UserRepository 
UserServiceImpl  ..>  UserService 
UserXml "1" *--> "role 1" Role 
UserXmlBuilder "1" *--> "role 1" Role 
UserXml  -->  UserXmlBuilder 
UserXmlRepository  ..>  UserDatabase 
UsersXmlWrapper "1" *--> "users *" UserXml 
ValidateAndCreateCourseDelegate "1" *--> "newCourseService 1" NewCourseService 
ValidateAndCreateExerciseDelegate "1" *--> "newExerciseService 1" NewExerciseService 
ValidateAndCreateModuleDelegate "1" *--> "newModuleService 1" NewModuleService 
ValidateExerciseDelegate "1" *--> "newExerciseService 1" NewExerciseService 
ValidateExerciseDelegate "1" *--> "studentRepository 1" StudentRepository 
ValidateExerciseDelegate "1" *--> "userService 1" UserService 
ValidateFinishingCourseDelegate "1" *--> "newCourseService 1" NewCourseService 
ValidateFinishingCourseDelegate "1" *--> "userService 1" UserService 
ValidateUserRoleDelegate "1" *--> "userService 1" UserService 

```