package pl.pawel.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.pawel.exceptions.UserServiceException;
import pl.pawel.io.entity.AddressEntity;
import pl.pawel.io.entity.UserEntity;
import pl.pawel.io.repository.UserRepository;
import pl.pawel.shared.Utils;
import pl.pawel.shared.dto.AddressDto;
import pl.pawel.shared.dto.UserDto;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    Utils utils;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    String userId ="w345es54";
    String encryptedPassword = "f34tst4";
    UserEntity userEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("Pablo");
        userEntity.setLastName("Escobar");
        userEntity.setEmail("test@test.com");
        userEntity.setUserId(userId);
        userEntity.setEncryptedPassword(encryptedPassword);
        userEntity.setEmailVerificationToken("aw35awtr4");
        userEntity.setAddresses(getAddressesEntity());
    }

    @Test
    public void getUser_basic() {
        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

        UserDto userDto = userService.getUser("test@test.com");

        assertNotNull(userDto);
        assertEquals("Pablo", userDto.getFirstName());
    }

    @Test
    public void getUser_UsernameNotFoundException() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class,
                     ()-> userService.getUser("test@test.com")
        );
    }

    @Test
    public void createUser_basic() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(utils.generateAddressId(30)).thenReturn("e456sry54");
        when(utils.generateUserId(30)).thenReturn(userId);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserDto userDto = new UserDto();
        userDto.setAddresses(getAddressDtos());
        userDto.setFirstName("Paulo");
        userDto.setLastName("Escobar");
        userDto.setPassword("12345678");
        userDto.setEmail("test@test.com");

        UserDto storedUserDetails = userService.createUser(userDto);

        assertNotNull(storedUserDetails);
        assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());
        assertEquals(userEntity.getLastName(), storedUserDetails.getLastName());
        assertNotNull(storedUserDetails.getUserId());
        assertEquals(storedUserDetails.getAddresses().size(), userEntity.getAddresses().size());
        verify(utils, times(storedUserDetails.getAddresses().size())).generateAddressId(30);
        verify(bCryptPasswordEncoder, times(1)).encode("12345678");
        verify(userRepository, times(1)).save(any(UserEntity.class));

    }

    @Test
    final void testCreateUser_CreateUserServiceException()
    {
        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
        UserDto userDto = new UserDto();
        userDto.setAddresses(getAddressDtos());
        userDto.setFirstName("Paulo");
        userDto.setLastName("Escobar");
        userDto.setPassword("12345678");
        userDto.setEmail("test@test.com");

        assertThrows(UserServiceException.class,
            () ->userService.createUser(userDto)
        );
    }

    private List<AddressDto> getAddressDtos() {
        AddressDto addressDto = new AddressDto();
        addressDto.setType("shipping");
        addressDto.setCity("Vancouver");
        addressDto.setCountry("Canada");
        addressDto.setPostalCode("20-209");
        addressDto.setStreetName("123 Street name");

        AddressDto billingAddressDto = new AddressDto();
        addressDto.setType("billing");
        addressDto.setCity("Vancouver");
        addressDto.setCountry("Canada");
        addressDto.setPostalCode("20-209");
        addressDto.setStreetName("123 Street name");

        List<AddressDto> addresses = new ArrayList<>();
        addresses.add(addressDto);
        addresses.add(billingAddressDto);
        return addresses;
    }

    private List<AddressEntity> getAddressesEntity() {
        List<AddressDto> addresses = getAddressDtos();
        Type listType = new TypeToken<List<AddressEntity>>() {}.getType();
        return new ModelMapper().map(addresses, listType);
    }
}