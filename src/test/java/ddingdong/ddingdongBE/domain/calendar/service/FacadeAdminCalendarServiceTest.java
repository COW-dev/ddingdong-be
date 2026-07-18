package ddingdong.ddingdongBE.domain.calendar.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import ddingdong.ddingdongBE.common.fixture.CategoryFixture;
import ddingdong.ddingdongBE.common.fixture.EventFixture;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.calendar.entity.Category;
import ddingdong.ddingdongBE.domain.calendar.entity.Event;
import ddingdong.ddingdongBE.domain.calendar.entity.RepeatType;
import ddingdong.ddingdongBE.domain.calendar.repository.CategoryRepository;
import ddingdong.ddingdongBE.domain.calendar.repository.EventRepository;
import ddingdong.ddingdongBE.domain.calendar.service.dto.command.CreateCategoryCommand;
import ddingdong.ddingdongBE.domain.calendar.service.dto.command.CreateEventCommand;
import ddingdong.ddingdongBE.domain.calendar.service.dto.command.UpdateEventCommand;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import ddingdong.ddingdongBE.domain.calendar.service.dto.query.EventQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FacadeAdminCalendarServiceTest extends TestContainerSupport {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FacadeAdminCalendarService facadeAdminCalendarService;

    private CreateCategoryCommand createCategoryCommand;

    @BeforeEach
    void setUp() {
        createCategoryCommand = new CreateCategoryCommand("testCategory", "#FFFFFF");
    }

    @DisplayName("어드민: 카테고리 생성")
    @Test
    void createCategory() {
        // given
        // when
        facadeAdminCalendarService.createCategory(createCategoryCommand);

        List<Category> categories = categoryRepository.findAll();

        // then
        assertThat(categories).hasSize(1);
        Category testCategory = categories.get(0);
        assertThat(testCategory.getName()).isEqualTo(createCategoryCommand.name());
        assertThat(testCategory.getColor()).isEqualTo(createCategoryCommand.color());
    }

    @DisplayName("어드민: 이벤트 생성")
    @Test
    void createEvent() {
        // given
        Category category = categoryRepository.save(CategoryFixture.createCategory());
        CreateEventCommand command = new CreateEventCommand(
                "testEvent",
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 2),
                RepeatType.NONE,
                category.getName(),
                "#FFFFFF"
        );

        // when
        facadeAdminCalendarService.createEvent(command);
        List<Event> events = eventRepository.findAll();

        // then
        assertThat(events).hasSize(1);
        Event testEvent = events.get(0);
        assertAll(
                () -> assertThat(testEvent.getTitle()).isEqualTo(command.title()),
                () -> assertThat(testEvent.getStartDate()).isEqualTo(command.startDate()),
                () -> assertThat(testEvent.getEndDate()).isEqualTo(command.endDate()),
                () -> assertThat(testEvent.getRepeatType()).isEqualTo(command.repeatType()),
                () -> assertThat(testEvent.getCategory().getId()).isEqualTo(category.getId()),
                () -> assertThat(testEvent.getCategory().getColor()).isEqualTo(command.color())
        );
    }

    @DisplayName("어드민: 캘린더 조회 시 해당 연/월과 겹치는 이벤트만 반환한다")
    @Test
    void getCalendar() {
        // given
        Category category = categoryRepository.save(CategoryFixture.createCategory());

        Event julyEvent = EventFixture.createEvent(
                category, LocalDate.of(2026, 7, 10), LocalDate.of(2026, 7, 12));
        Event augustEvent = EventFixture.createEvent(
                category, LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 2));
        eventRepository.saveAll(List.of(julyEvent, augustEvent));

        // when
        List<EventQuery> result = facadeAdminCalendarService.getCalendar(2026, 7);

        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result.get(0).id()).isEqualTo(julyEvent.getId())
        );
    }

    @DisplayName("어드민: 이벤트 상세조회")
    @Test
    void getEvent() {
        // given
        Category category = categoryRepository.save(CategoryFixture.createCategory());
        Event event = eventRepository.save(EventFixture.createEvent(category));

        // when
        EventQuery result = facadeAdminCalendarService.getEvent(event.getId());

        // then
        assertAll(
                () -> assertThat(result.id()).isEqualTo(event.getId()),
                () -> assertThat(result.title()).isEqualTo(event.getTitle()),
                () -> assertThat(result.startDate()).isEqualTo(event.getStartDate()),
                () -> assertThat(result.endDate()).isEqualTo(event.getEndDate()),
                () -> assertThat(result.repeatType()).isEqualTo(event.getRepeatType()),
                () -> assertThat(result.categoryName()).isEqualTo(category.getName()),
                () -> assertThat(result.color()).isEqualTo(category.getColor())
        );
    }

    @DisplayName("어드민: 이벤트 수정")
    @Test
    void updateEvent() {
        // given
        Category category = categoryRepository.save(CategoryFixture.createCategory());
        Category newCategory = categoryRepository.save(
                CategoryFixture.createCategory("newCategory", "#000000"));
        Event event = eventRepository.save(EventFixture.createEvent(category));

        UpdateEventCommand updateCommand = new UpdateEventCommand(
                "updatedTitle",
                LocalDate.of(2026, 7, 5),
                LocalDate.of(2026, 7, 6),
                RepeatType.WEEKLY,
                newCategory.getName(),
                "#123456"
        );

        // when
        facadeAdminCalendarService.updateEvent(event.getId(), updateCommand);
        Event updatedEvent = eventRepository.findById(event.getId()).orElseThrow();

        // then
        assertAll(
                () -> assertThat(updatedEvent.getTitle()).isEqualTo(updateCommand.title()),
                () -> assertThat(updatedEvent.getStartDate()).isEqualTo(updateCommand.startDate()),
                () -> assertThat(updatedEvent.getEndDate()).isEqualTo(updateCommand.endDate()),
                () -> assertThat(updatedEvent.getRepeatType()).isEqualTo(updateCommand.repeatType()),
                () -> assertThat(updatedEvent.getCategory().getId()).isEqualTo(newCategory.getId())
        );
    }

    @DisplayName("어드민: 이벤트 삭제")
    @Test
    void deleteEvent() {
        // given
        Category category = categoryRepository.save(CategoryFixture.createCategory());
        Event event = eventRepository.save(EventFixture.createEvent(category));

        // when
        facadeAdminCalendarService.deleteEvent(event.getId());
        Optional<Event> result = eventRepository.findById(event.getId());

        // then
        assertThat(result).isEmpty();
    }
}