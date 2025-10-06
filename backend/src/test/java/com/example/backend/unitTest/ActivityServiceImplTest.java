package com.example.backend.unitTest;

import com.example.backend.dto.ActivityDTO;
import com.example.backend.dto.ActivityMapper;
import com.example.backend.exception.NotFoundException;
import com.example.backend.model.Activity;
import com.example.backend.repository.ActivityRepository;
import com.example.backend.service.ActivityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ActivityServiceImplTest {


    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private ActivityMapper activityMapper;

    @InjectMocks
    private ActivityServiceImpl activityServiceImpl;

    private Activity activity;
    private Activity activity2;
    private ActivityDTO.activityDto activityDto;
    private ActivityDTO.activityDto activityDto2;



    @BeforeEach
    void setUp() {
        activityDto = new ActivityDTO.activityDto(
                1L,
                "GoKart",
                "Outdoor GoKart racing track",
                12,
                140,
                8);


        activityDto2 = new ActivityDTO.activityDto(
                2L,
                "Bowling",
                "Fun game",
                8,
                120,
                6
        );

        activity = new Activity(
                1L,
                "GoKart",
                "Outdoor GoKart racing track",
                12,
                140,
                8);

        activity2 = new Activity(
                2L,
                "Bowling",
                "Fun game",
                8,
                120,
                6
        );
    }


    // Tests that createActivity() maps DTO to entity, saves it and returns the saved activity as a DTO
    @Test
    void createActivity_ShouldSaveAndReturnDto() {

        // Arrange
        when(activityMapper.toEntity(any())).thenReturn(activity);
        when(activityRepository.save(any())).thenReturn(activity);
        when(activityMapper.toDto(any())).thenReturn(activityDto);

        // Act
        ActivityDTO.activityDto result = activityServiceImpl.createActivity(activityDto);

        // Assert
       assertNotNull(result);
       assertEquals(1L,result.id());
       assertEquals("GoKart", result.name());
       verify(activityRepository, times(1)).save(any(Activity.class));
       verify(activityMapper, times(1)).toEntity(any());
       verify(activityMapper, times(1)).toDto(any());
    }


    // Tests that getAllActivities() returns all activities mapped to DTOs
    @Test
    void getAllActivities_ShouldReturnListOfDtos() {

        // Arrange
        when(activityRepository.findAll()).thenReturn(List.of(activity,activity2));
        when(activityMapper.toDto(activity)).thenReturn(activityDto);
        when(activityMapper.toDto(activity2)).thenReturn(activityDto2);

        // Act
        List<ActivityDTO.activityDto> result = activityServiceImpl.getAllActivities();

        // Assert
        assertNotNull(result);
        assertEquals(2,result.size());
        assertEquals(1L, result.getFirst().id());
        assertEquals(2L, result.get(1).id());
        assertEquals("GoKart",result.getFirst().name());
        assertEquals("Bowling",result.get(1).name());
    }

    // Tests that getAllActivities() returns an empty list, when there are no activities, instead of Null
    @Test
    void getAllActivities_WhenNoActivities_ShouldReturnEmptyList() {

        // Arrange
        when(activityRepository.findAll()).thenReturn(List.of());

        // Act
        List<ActivityDTO.activityDto> result = activityServiceImpl.getAllActivities();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty(), "Result list should be empty");
    }

    // Tests that getActivityById() returns an activity if the Id exists in DB
    @Test
    void getActivityById_WhenExists_ShouldReturnDto() {

        // Arrange
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
        when(activityMapper.toDto(activity)).thenReturn(activityDto);

        // Act
        ActivityDTO.activityDto result = activityServiceImpl.getActivityById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L,result.id());
        assertEquals("GoKart", result.name());
        verify(activityRepository, times(1)).findById(1L);
    }


    // Tests that getActivityById() throws NotFoundException if the activity does not exists
    @Test
    void getActivityById_WhenNotFound_ShouldReturnException() {

        // Arrange
        when(activityRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> activityServiceImpl.getActivityById(999L));
        verify(activityRepository, times(1)).findById(999L);
    }

    // Tests that deleteActivity() deletes an activity when it exists in the repository
    @Test
    void deleteActivity_WhenExists_ShouldDeleteSuccessfully() {

        // Arrange
        Long activityId = activity.getId();
        when(activityRepository.existsById(activityId)).thenReturn(true);

        // Act
        activityServiceImpl.deleteActivity(activityId);

        // Assert
        verify(activityRepository, times(1)).existsById(activityId);
        verify(activityRepository, times(1)).deleteById(activityId);
        verifyNoMoreInteractions(activityRepository);
    }


    // Tests that deleteActivity() throws NotFoundException when the activity does not exist
    @Test
    void deleteActivity_WhenNotFound_ShouldThrowException() {

        // Arrange
        Long activityId = 999L;
        when(activityRepository.existsById(activityId)).thenReturn(false);

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> activityServiceImpl.deleteActivity(activityId));

        assertEquals("Activity not found with id: 999", exception.getMessage());
        verify(activityRepository, times(1)).existsById(activityId);
        verify(activityRepository, never()).deleteById(anyLong());
    }

}
