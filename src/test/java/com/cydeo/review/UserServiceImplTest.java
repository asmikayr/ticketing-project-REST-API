package com.cydeo.review;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.RoleDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Role;
import com.cydeo.entity.User;
import com.cydeo.exception.TicketingProjectException;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.KeycloakService;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.impl.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private ProjectService projectService;
    @Mock
    private TaskService taskService;
    @Mock
    private KeycloakService keycloakService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    User user;
    UserDTO userDTO;


//    @BeforeAll
//    public static void setUpBeforeClass() throws Exception{
//        //Some impl
//    }
//
//    @AfterAll
//    public static void tearDownAfterClass() throws Exception{
//        //some impl
//    }

    @BeforeEach
    public void setUp() throws Exception{
        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUserName("user");
        user.setPassWord("Abc1");
        user.setEnabled(true);
        user.setRole(new Role("Manager"));

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setUserName("user");
        userDTO.setPassWord("Abc1");
        userDTO.setEnabled(true);
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setDescription("Manager");
        userDTO.setRole(roleDTO);
    }

    private List<User> getUsers(){
        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Emily");
        return List.of(user, user2);
    }

    private List<UserDTO> getUserDTOs(){
        UserDTO userDTO2 = new UserDTO();
        userDTO2.setId(2L);
        userDTO2.setFirstName("Emily");
        return List.of(userDTO, userDTO2);
    }

    private User getUserWithRole(String role){
        User user3 = new User();
        user3.setUserName("user3");
        user3.setPassWord("Abc1");
        user3.setEnabled(true);
        user3.setIsDeleted(false);
        user3.setRole(new Role(role));
        return user3;
    }

    @AfterEach
    public void tearDown() throws Exception{
        //Some impl
    }

    //TestNG

    @Test
    public void test(){

    }

    @Test
    public void should_list_all_users(){
        //Stubbing _ giving an impl
        //given - Preparation
        when(userRepository.findAllByIsDeletedOrderByFirstNameDesc(false)).thenReturn(getUsers());
        when(userMapper.convertToDto(user)).thenReturn(userDTO);
        when(userMapper.convertToDto(getUsers().get(1))).thenReturn(getUserDTOs().get(1));

       // when(userMapper.convertToDto(any(User.class))).thenReturn(userDTO, getUserDTOs().get(1));

        List<UserDTO> expectedList = getUserDTOs();
        //when-action
        List<UserDTO> actualList = userService.listAllUsers();
        //then - Assertion/Verification
        //assertEquals(expectedList, actualList);

        // AssertJ
        assertThat(actualList).usingRecursiveComparison().isEqualTo(expectedList);

        verify(userRepository, times(1)).findAllByIsDeletedOrderByFirstNameDesc(false);
        verify(userRepository, never()).findAllByIsDeletedOrderByFirstNameDesc(true);

    }

    @Test
    public void should_throw_nosuchelementexception_when_user_not_found(){
        //given - We don't need it as a mock already return null
        //when(userRepository.findByUserNameAndIsDeleted(anyString(), anyBoolean())).thenReturn(null);
        //if it throws an exception we are not going to this step
       // when(userMapper.convertToDto(any(User.class))).thenReturn(userDTO);
        //action

        //when+then

       Throwable actualException = assertThrowsExactly(NoSuchElementException.class, () -> userService.findByUserName("SomeUserName"));
        assertEquals("User not found.", actualException.getMessage());
    //AssertJ
       // Throwable actualException = catchThrowable(()-> userService.findByUserName("SomeUserName"));

    }

    /*
        //     User Story - 1: As a user of the application, I want my password to be encoded
    //    so that my account remains secure.
    //
    //    Acceptance Criteria:
    //    1 - When a user creates a new account, their password should be encoded using
    //    a secure algorithm such as bcrypt or PBKDF2.
    //
    //    2 - Passwords should not be stored in plain text in the database or any other storage.
    //
    //    3 - Passwords encoding should be implemented consistently throughout the application,
    //    including any password reset or change functionality.
     */

    @Test
    void should_encode_user_password_on_save_operation(){
        //given
        when(userMapper.convertToEntity(any(UserDTO.class))).thenReturn(user);
        when(userRepository.save(any())).thenReturn(user);
        when(userMapper.convertToDto(any(User.class))).thenReturn(userDTO);
        when(passwordEncoder.encode(anyString())).thenReturn("some-password");

        String expectedPassword = "some-password";

        //when
        UserDTO savedUser = userService.save(userDTO);

        //then
        assertEquals(expectedPassword, savedUser.getPassWord());
        //verify that passwordEncoder is executed
        verify(passwordEncoder).encode(anyString());
    }

    @Test
    void should_encode_user_password_on_update_operation(){
        //given
        when(userRepository.findByUserNameAndIsDeleted(anyString(), anyBoolean())).thenReturn(user);
        when(userMapper.convertToEntity(any(UserDTO.class))).thenReturn(user);
        when(userRepository.save(any())).thenReturn(user);
        when(userMapper.convertToDto(any(User.class))).thenReturn(userDTO);
        when(passwordEncoder.encode(anyString())).thenReturn("some-password");

        String expectedPassword = "some-password";

        //when
        UserDTO updatedUser = userService.update(userDTO);

        //then
        assertEquals(expectedPassword, updatedUser.getPassWord());
        //verify that passwordEncoder is executed
        verify(passwordEncoder).encode(anyString());

    }

    @Test
    void should_delete_manager() throws TicketingProjectException {
        //given
        User managerUser = getUserWithRole("Manager");
        when(userRepository.findByUserNameAndIsDeleted(anyString(), anyBoolean())).thenReturn(managerUser);
        when(userRepository.save(any())).thenReturn(managerUser);
        when(projectService.listAllNonCompletedByAssignedManager(any())).thenReturn(new ArrayList<>());
        //when
        userService.delete(managerUser.getUserName());
        //then
        assertTrue(managerUser.getIsDeleted());
        assertNotEquals("user3", managerUser.getUserName());
    }

    @Test
    void should_delete_employee() throws TicketingProjectException {
        //given
        User employeeUser = getUserWithRole("Employee");
        when(userRepository.findByUserNameAndIsDeleted(anyString(), anyBoolean())).thenReturn(employeeUser);
        when(userRepository.save(any())).thenReturn(employeeUser);
        when(taskService.listAllNonCompletedByAssignedEmployee(any())).thenReturn(new ArrayList<>());
        //when
        userService.delete(employeeUser.getUserName());
        //then
        assertTrue(employeeUser.getIsDeleted());
        assertNotEquals("user3", employeeUser.getUserName());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Manager", "Employee"})
    void should_delete_user(String role) throws TicketingProjectException {
        //given
        User testUser = getUserWithRole(role);
        when(userRepository.findByUserNameAndIsDeleted(anyString(), anyBoolean())).thenReturn(testUser);
        when(userRepository.save(any())).thenReturn(testUser);

//       if(testUser.getRole().getDescription().equals("Manager")) {
//           when(projectService.listAllNonCompletedByAssignedManager(any())).thenReturn(new ArrayList<>());
//       }else if(testUser.getRole().getDescription().equals("Employee")){
//           when(taskService.listAllNonCompletedByAssignedEmployee(any())).thenReturn(new ArrayList<>());
//        }

       lenient().when(projectService.listAllNonCompletedByAssignedManager(any())).thenReturn(new ArrayList<>());
       lenient().when(taskService.listAllNonCompletedByAssignedEmployee(any())).thenReturn(new ArrayList<>());

            userService.delete(testUser.getUserName());
        assertTrue(testUser.getIsDeleted());
        assertNotEquals("user3", testUser.getUserName());
    }

    @Test
    void should_throw_exception_when_deleting_manager_with_project(){
        User managerUser = getUserWithRole("Manager");
        when(userRepository.findByUserNameAndIsDeleted(anyString(), anyBoolean())).thenReturn(managerUser);
        when(projectService.listAllNonCompletedByAssignedManager(any())).thenReturn(List.of(new ProjectDTO(), new ProjectDTO()));

        Throwable actualException = assertThrows(TicketingProjectException.class, () -> userService.delete(managerUser.getUserName()));
        assertEquals("User can not be deleted", actualException.getMessage());


    }

    @Test
    void should_throw_exception_when_deleting_employee_with_task(){
        User employeeUser = getUserWithRole("Employee");
        when(userRepository.findByUserNameAndIsDeleted(anyString(), anyBoolean())).thenReturn(employeeUser);
        when(taskService.listAllNonCompletedByAssignedEmployee(any())).thenReturn(List.of(new TaskDTO(), new TaskDTO()));

        Throwable actualException = assertThrows(TicketingProjectException.class, () -> userService.delete(employeeUser.getUserName()));
        assertEquals("User can not be deleted", actualException.getMessage());
    }




}
