/*
package com.aitravel.application;


import com.aitravel.application.model.Budget;
import com.aitravel.application.model.Day;
import com.aitravel.application.model.Itinerary;
import com.aitravel.application.model.User;
import com.aitravel.application.repositoryjpa.DayRepository;
import com.aitravel.application.repositoryjpa.ItineraryRepository;
import com.aitravel.application.repositoryjpa.UserRepository;
import com.aitravel.application.service.ItineraryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
public class ItineraryDeletionTests {
    @Autowired
    private ItineraryService itineraryService;

    @Autowired
    private ItineraryRepository itineraryRepository;

    @Autowired
    private DayRepository dayRepository;
    @Autowired
    private UserRepository userRepository;

    // Test 1: Verify that the itinerary is deleted.
    @Test
    public void testDeleteItinerary_RemovesItinerary() {
        User testUser = new User();
        testUser.setUserId("89e93645bb1a4ad2a572a9b799174e56"); // or set using UUID if your id is UUID
        testUser.setName("Test User");
        testUser.setPassword("mahesh@16789");
        testUser.setEmail("maheshdondapatidon@gmail.com");
        testUser.setAvatarImgUrl("http://google.com");
        testUser.setUpdatedAt(LocalDateTime.now());
        testUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(testUser);
        // Create an itinerary with a child Day and Budget
        Itinerary itinerary = new Itinerary();
        itinerary.setUserId(testUser.getUserId());
        itinerary.setTitle("Test Trip");
        itinerary.setStatus("draft");
        itinerary.setTripImg("");
        itinerary.setVisibility("private");
        itinerary.setCreatedAt(LocalDateTime.now());
        itinerary.setUpdatedAt(LocalDateTime.now());
        itinerary.setStartDate(LocalDate.now());
        itinerary.setEndDate(LocalDate.now().plusDays(3));
        // Set destination details (flattened into itinerary)
        itinerary.setDestinationName("Test Destination");
        itinerary.setDestinationCoordinates("12.34,56.78");

        // Create and assign a Budget (OneToOne)
        Budget budget = new Budget();
        budget.setCurrency("USD");
        budget.setTotalAmount(BigDecimal.valueOf(1000.00));
        budget.setItinerary(itinerary);
        itinerary.setBudget(budget);

        // Create and add a Day to the itinerary (OneToMany)
        Day day = new Day();
        day.setDayNumber(1);
        day.setDate(LocalDate.now());
        day.setItinerary(itinerary);
        itinerary.getDays().add(day);

        // Save the itinerary. Cascading should ensure Budget and Day are persisted.
        itinerary = itineraryRepository.save(itinerary);
        UUID itineraryId = itinerary.getId();

        // Assert that itinerary and child are in the database
        assertThat(itineraryRepository.findById(itineraryId)).isPresent();
        assertThat(dayRepository.findAll()).isNotEmpty();

        // Now delete the itinerary through the service
        itineraryService.deleteItinerary(itineraryId);

        // Verify that the itinerary is gone
        assertThat(itineraryRepository.findById(itineraryId)).isNotPresent();
    }

    // Test 2: Verify that orphan child entities (Days) are removed after itinerary deletion.
    @Test
    public void testCascadeDeletion_RemovesOrphans() {

        User testUser = new User();
        testUser.setUserId("89e93645bb1a4ad2a572a9b799174e63"); // or set using UUID if your id is UUID
        testUser.setName("Test User");
        testUser.setPassword("mahesh@16789");
        testUser.setEmail("maheshdondapatidon@gmail.com");
        testUser.setAvatarImgUrl("http://google.com");
        testUser.setUpdatedAt(LocalDateTime.now());
        testUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(testUser);
        // Create an itinerary with two Days.
        Itinerary itinerary = new Itinerary();
        itinerary.setUserId(testUser.getUserId());
        itinerary.setTitle("Cascade Test Trip");
        itinerary.setStatus("draft");
        itinerary.setTripImg("");
        itinerary.setVisibility("private");
        itinerary.setCreatedAt(LocalDateTime.now());
        itinerary.setUpdatedAt(LocalDateTime.now());
        itinerary.setStartDate(LocalDate.now());
        itinerary.setEndDate(LocalDate.now().plusDays(2));
        itinerary.setDestinationName("Cascade Destination");
        itinerary.setDestinationCoordinates("11.11,22.22");

        Day day1 = new Day();
        day1.setDayNumber(1);
        day1.setDate(LocalDate.now());
        day1.setItinerary(itinerary);

        Day day2 = new Day();
        day2.setDayNumber(2);
        day2.setDate(LocalDate.now().plusDays(1));
        day2.setItinerary(itinerary);

        itinerary.getDays().add(day1);
        itinerary.getDays().add(day2);

        itinerary = itineraryRepository.save(itinerary);
        UUID itineraryId = itinerary.getId();

        // Make sure both Days are saved
        assertThat(dayRepository.findAll()).extracting(Day::getItinerary)
                .anyMatch(it -> it.getId().equals(itineraryId));

        // Delete the itinerary via the service; orphanRemoval on days will remove them.
        itineraryService.deleteItinerary(itineraryId);

        // Verify that the days for that itinerary have been removed.
        // You can either query the dayRepository for any rows with the given itinerary,
        // or simply observe that overall day count decreased.
        // For example, check that no Day exists with the itinerary id:
        assertThat(dayRepository.findAll())
                .noneMatch(day -> day.getItinerary() != null && day.getItinerary().getId().equals(itineraryId));
    }
}
*/
