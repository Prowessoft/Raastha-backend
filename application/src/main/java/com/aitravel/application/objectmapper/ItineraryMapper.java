package com.aitravel.application.objectmapper;

import com.aitravel.application.dto.ItineraryDTO;
import com.aitravel.application.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ItineraryMapper {

    // Converts an Itinerary entity to a full DTO with nested data
    public ItineraryDTO toDTO(Itinerary itinerary) {
        log.info("Starting mapping of Itinerary to ItineraryDTO");
        try {
            if (itinerary == null) {
                log.warn("Provided itinerary is null. Returning null DTO.");
                return null;
            }
            ItineraryDTO dto = new ItineraryDTO();
            dto.setId(itinerary.getId());
            dto.setUserId(itinerary.getUserId());
            dto.setTripImg(itinerary.getTripImg());
            dto.setTitle(itinerary.getTitle());
            dto.setStatus(itinerary.getStatus());
            dto.setVisibility(itinerary.getVisibility());
            dto.setCreatedAt(itinerary.getCreatedAt());
            dto.setUpdatedAt(itinerary.getUpdatedAt());

            // Map trip details
            log.debug("Mapping trip details");
            ItineraryDTO.TripDetailsDTO td = new ItineraryDTO.TripDetailsDTO();
            ItineraryDTO.DestinationDTO dest = new ItineraryDTO.DestinationDTO();
            dest.setName(itinerary.getDestinationName());
            if (itinerary.getDestinationCoordinates() != null) {
                String[] parts = itinerary.getDestinationCoordinates().split(",");
                List<Double> coords = new ArrayList<>();
                for (String part : parts) {
                    try {
                        coords.add(Double.parseDouble(part.trim()));
                    } catch (NumberFormatException e) {
                        log.error("Error parsing destination coordinate '{}': {}", part, e.getMessage());
                    }
                }
                dest.setCoordinates(coords);
            }
            td.setDestination(dest);
            td.setStartDate(itinerary.getStartDate());
            td.setEndDate(itinerary.getEndDate());
            if (itinerary.getBudget() != null) {
                log.debug("Mapping budget details");
                ItineraryDTO.TripBudgetDTO tbd = new ItineraryDTO.TripBudgetDTO();
                tbd.setCurrency(itinerary.getBudget().getCurrency());
                tbd.setTotal(itinerary.getBudget().getTotalAmount().doubleValue());
                ItineraryDTO.BudgetBreakdownDTO bbd = new ItineraryDTO.BudgetBreakdownDTO();
                bbd.setAccommodation(itinerary.getBudget().getAccommodationAmount().doubleValue());
                bbd.setActivities(itinerary.getBudget().getActivitiesAmount().doubleValue());
                bbd.setDining(itinerary.getBudget().getDiningAmount().doubleValue());
                bbd.setTransport(itinerary.getBudget().getTransportAmount().doubleValue());
                tbd.setBreakdown(bbd);
                td.setBudget(tbd);
            }
            dto.setTripDetails(td);

            // Map days
            if (itinerary.getDays() != null) {
                log.debug("Mapping {} days", itinerary.getDays().size());
                List<ItineraryDTO.DayDTO> dayDTOs = new ArrayList<>();
                for (Day d : itinerary.getDays()) {
                    log.debug("Mapping Day with id: {} and dayNumber: {}", d.getId(), d.getDayNumber());
                    ItineraryDTO.DayDTO dayDto = new ItineraryDTO.DayDTO();
                    dayDto.setId(d.getId().toString());
                    dayDto.setDayNumber(d.getDayNumber());
                    dayDto.setDate(d.getDate());
                    ItineraryDTO.DayBudgetDTO dbd = new ItineraryDTO.DayBudgetDTO();
                    if (d.getPlannedBudget() != null)
                        dbd.setPlanned(d.getPlannedBudget().doubleValue());
                    if (d.getActualBudget() != null)
                        dbd.setActual(d.getActualBudget().doubleValue());
                    dayDto.setBudget(dbd);
                    dayDto.setNotes(d.getNotes());

                    // Map sections from activities
                    if (d.getActivities() != null) {
                        log.debug("Mapping activities sections for Day id: {}", d.getId());
                        ItineraryDTO.SectionsDTO sections = new ItineraryDTO.SectionsDTO();
                        List<ItineraryDTO.PlaceDTO> hotels = new ArrayList<>();
                        List<ItineraryDTO.PlaceDTO> acts = new ArrayList<>();
                        List<ItineraryDTO.PlaceDTO> restaurants = new ArrayList<>();

                        for (Activity act : d.getActivities()) {
                            ItineraryDTO.PlaceDTO pdto = new ItineraryDTO.PlaceDTO();
                            pdto.setId(act.getPlace().getId().toString());
                            pdto.setTitle(act.getPlace().getName());
                            pdto.setDescription(act.getPlace().getDescription());
                            pdto.setType(act.getPlace().getPlaceType());

                            // Map location
                            ItineraryDTO.LocationDTO ldto = new ItineraryDTO.LocationDTO();
                            if (act.getPlace().getCoordinates() != null) {
                                String[] coords = act.getPlace().getCoordinates().split(",");
                                List<Double> coList = new ArrayList<>();
                                for (String c : coords) {
                                    try {
                                        coList.add(Double.parseDouble(c.trim()));
                                    } catch (NumberFormatException e) {
                                        log.error("Error parsing activity coordinate '{}': {}", c, e.getMessage());
                                    }
                                }
                                ldto.setCoordinates(coList);
                            }
                            ldto.setName(act.getPlace().getName());
                            ldto.setPlaceId(act.getPlace().getGooglePlaceId());
                            pdto.setLocation(ldto);

                            pdto.setStartTime(act.getStartTime() != null ? act.getStartTime().toString() : null);
                            pdto.setEndTime(act.getEndTime() != null ? act.getEndTime().toString() : null);
                            pdto.setDuration(act.getDuration());
                            pdto.setPrice(act.getPrice());

                            // Map contact info
                            ItineraryDTO.ContactDTO cdto = new ItineraryDTO.ContactDTO();
                            cdto.setPhone(act.getPlace().getPhone());
                            cdto.setWebsite(act.getPlace().getWebsite());
                            cdto.setGoogleMapsUrl(act.getPlace().getGoogleMapsUrl());
                            pdto.setContact(cdto);

                            // Map photos for the place
                            if (act.getPlace().getPhotos() != null && !act.getPlace().getPhotos().isEmpty()) {
                                List<ItineraryDTO.PhotoDTO> photos = new ArrayList<>();
                                act.getPlace().getPhotos().forEach(photo -> {
                                    ItineraryDTO.PhotoDTO pd = new ItineraryDTO.PhotoDTO();
                                    pd.setUrl(photo.getUrl());
                                    pd.setCaption(photo.getCaption());
                                    photos.add(pd);
                                });
                                pdto.setPhotos(photos);
                            }

                            // Map operating hours
                            if (act.getPlace().getOperatingHours() != null && !act.getPlace().getOperatingHours().isEmpty()) {
                                ItineraryDTO.OperatingHoursDTO ohDTO = new ItineraryDTO.OperatingHoursDTO();
                                List<ItineraryDTO.OperatingPeriodDTO> periods = new ArrayList<>();
                                act.getPlace().getOperatingHours().forEach(oh -> {
                                    ItineraryDTO.OperatingPeriodDTO opdto = new ItineraryDTO.OperatingPeriodDTO();
                                    opdto.setDay(oh.getDayOfWeek());
                                    opdto.setHours(oh.getOpenTime() + "-" + oh.getCloseTime());
                                    periods.add(opdto);
                                });
                                ohDTO.setIsOpen(true); // Derive isOpen according to business rules.
                                ohDTO.setPeriods(periods);
                                pdto.setOperatingHours(ohDTO);
                            }

                            // Map booking info
                            if (act.getBookingInfo() != null) {
                                ItineraryDTO.BookingInfoDTO biDTO = new ItineraryDTO.BookingInfoDTO();
                                biDTO.setConfirmationNumber(act.getBookingInfo().getConfirmationNumber());
                                biDTO.setReservationNumber(act.getBookingInfo().getReservationNumber());
                                biDTO.setNotes(act.getBookingInfo().getNotes());
                                pdto.setBookingInfo(biDTO);
                            }

                            // Add to correct section list based on type.
                            String type = act.getPlace().getPlaceType();
                            if ("hotel".equalsIgnoreCase(type)) {
                                hotels.add(pdto);
                            } else if ("restaurant".equalsIgnoreCase(type)) {
                                restaurants.add(pdto);
                            } else if ("activity".equalsIgnoreCase(type)) {
                                acts.add(pdto);
                            }
                        }
                        sections.setHotels(hotels);
                        sections.setRestaurants(restaurants);
                        sections.setActivities(acts);
                        dayDto.setSections(sections);
                    }

                    // Map travelInfo from travel routes
                    if (d.getTravelRoutes() != null && !d.getTravelRoutes().isEmpty()) {
                        log.debug("Mapping travel info for Day id: {}", d.getId());
                        ItineraryDTO.TravelInfoDTO travelInfoDTO = new ItineraryDTO.TravelInfoDTO();
                        travelInfoDTO.setMode(d.getTravelRoutes().get(0).getTravelMode());
                        List<ItineraryDTO.RouteDTO> routeDTOList = new ArrayList<>();
                        d.getTravelRoutes().forEach(tr -> {
                            ItineraryDTO.RouteDTO routeDTO = new ItineraryDTO.RouteDTO();
                            routeDTO.setFrom(tr.getFromActivity() != null && tr.getFromActivity().getPlace() != null ?
                                    tr.getFromActivity().getPlace().getId().toString() : null);
                            routeDTO.setTo(tr.getToActivity() != null && tr.getToActivity().getPlace() != null ?
                                    tr.getToActivity().getPlace().getId().toString() : null);
                            routeDTO.setDistance(tr.getDistance());
                            routeDTO.setDuration(tr.getDuration());
                            routeDTO.setPolyline(tr.getPolyline());
                            routeDTOList.add(routeDTO);
                        });
                        travelInfoDTO.setRoutes(routeDTOList);
                        dayDto.setTravelInfo(travelInfoDTO);
                    }

                    dayDTOs.add(dayDto);
                }
                dto.setDays(dayDTOs);
            }

            // Map sharedWith from sharedAccess
            if (itinerary.getSharedAccess() != null) {
                log.debug("Mapping shared access details");
                List<ItineraryDTO.SharedWithDTO> shared = new ArrayList<>();
                itinerary.getSharedAccess().forEach(sa -> {
                    ItineraryDTO.SharedWithDTO sw = new ItineraryDTO.SharedWithDTO();
                    sw.setUserId(sa.getUserId());
                    sw.setEmail(sa.getEmail());
                    sw.setPermission(sa.getPermission());
                    shared.add(sw);
                });
                dto.setSharedWith(shared);
            }

            // Map metadata – tags and lastSavedBy are omitted if not persisted.
            ItineraryDTO.MetadataDTO meta = new ItineraryDTO.MetadataDTO();
            meta.setLanguage(itinerary.getLanguage());
            meta.setIsTemplate(itinerary.getIsTemplate());
            meta.setVersion(itinerary.getVersion());
            dto.setMetadata(meta);

            log.info("Completed mapping of Itinerary id: {} to ItineraryDTO", itinerary.getId());
            return dto;
        }
        catch (Exception e){
            log.error("Exception when it converting to DTO: " +e);
            return null;
        }
    }

    // Maps a new Itinerary from a DTO (used for create)
    public Itinerary toEntity(ItineraryDTO dto) {
        log.info("Starting mapping of ItineraryDTO to Itinerary entity");
        if (dto == null) {
            log.warn("Provided ItineraryDTO is null. Returning null entity.");
            return null;
        }
        Itinerary model = new Itinerary();
        if (dto.getId() != null) {
            model.setId(dto.getId());
        } else {
            model.setId(UUID.randomUUID());
            log.debug("Generated new UUID for Itinerary: {}", model.getId());
        }
        model.setUserId(dto.getUserId());
        model.setTitle(dto.getTitle());
        model.setStatus(dto.getStatus());
        model.setTripImg(dto.getTripImg());
        model.setVisibility(dto.getVisibility());
        model.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDateTime.now());
        model.setUpdatedAt(LocalDateTime.now());

        // Map trip details
        if (dto.getTripDetails() != null) {
            log.debug("Mapping trip details from DTO");
            if (dto.getTripDetails().getDestination() != null) {
                model.setDestinationName(dto.getTripDetails().getDestination().getName());
                if (dto.getTripDetails().getDestination().getCoordinates() != null) {
                    String coordStr = String.join(",", dto.getTripDetails().getDestination().getCoordinates()
                            .stream().map(Object::toString).toArray(String[]::new));
                    model.setDestinationCoordinates(coordStr);
                }
            }
            model.setStartDate(dto.getTripDetails().getStartDate());
            model.setEndDate(dto.getTripDetails().getEndDate());

            if (dto.getTripDetails().getBudget() != null) {
                log.debug("Mapping budget details from DTO");
                Budget budget = new Budget();
                budget.setCurrency(dto.getTripDetails().getBudget().getCurrency());
                budget.setTotalAmount(BigDecimal.valueOf(dto.getTripDetails().getBudget().getTotal()));
                budget.setAccommodationAmount(BigDecimal.valueOf(dto.getTripDetails().getBudget().getBreakdown().getAccommodation()));
                budget.setActivitiesAmount(BigDecimal.valueOf(dto.getTripDetails().getBudget().getBreakdown().getActivities()));
                budget.setDiningAmount(BigDecimal.valueOf(dto.getTripDetails().getBudget().getBreakdown().getDining()));
                budget.setTransportAmount(BigDecimal.valueOf(dto.getTripDetails().getBudget().getBreakdown().getTransport()));
                budget.setCreatedAt(LocalDateTime.now());
                budget.setUpdatedAt(LocalDateTime.now());
                budget.setItinerary(model);
                model.setBudget(budget);
            }
        }

        // Map days and their nested sections
        if (dto.getDays() != null) {
            log.debug("Mapping {} days from DTO to entity", dto.getDays().size());
            List<Day> days = new ArrayList<>();
            dto.getDays().forEach(dayDto -> {
                Day day = mapDaySectionsAndTravel(new Day(), dayDto);
                day.setItinerary(model);
                days.add(day);
            });
            model.setDays(days);
        }

        // Map sharedWith into SharedAccess
        if (dto.getSharedWith() != null) {
            log.debug("Mapping shared access from DTO");
            List<SharedAccess> sharedList = new ArrayList<>();
            dto.getSharedWith().forEach(s -> {
                SharedAccess sa = new SharedAccess();
                sa.setUserId(s.getUserId());
                sa.setEmail(s.getEmail());
                sa.setPermission(s.getPermission());
                sa.setCreatedAt(LocalDateTime.now());
                sa.setItinerary(model);
                sharedList.add(sa);
            });
            model.setSharedAccess(sharedList);
        }

        // Map metadata
        if (dto.getMetadata() != null) {
            log.debug("Mapping metadata from DTO");
            model.setIsTemplate(dto.getMetadata().getIsTemplate());
            model.setLanguage(dto.getMetadata().getLanguage());
            model.setVersion(dto.getMetadata().getVersion());
        }

        log.info("Completed mapping of ItineraryDTO to Itinerary entity with id: {}", model.getId());
        return model;
    }

    // Helper method to update an existing Itinerary with values from a DTO.
    public void updateEntity(Itinerary existing, ItineraryDTO dto) {
        log.info("Updating existing Itinerary id: {} with new DTO data", existing.getId());
        // Only update basic fields if provided
        if (dto.getTitle() != null) {
            existing.setTitle(dto.getTitle());
        }
        if (dto.getStatus() != null) {
            existing.setStatus(dto.getStatus());
        }
        if (dto.getVisibility() != null) {
            existing.setVisibility(dto.getVisibility());
        }
        // Do not update userId if not provided, as it’s the owner identifier

        // Update trip details if provided
        if (dto.getTripDetails() != null) {
            log.debug("Updating trip details for Itinerary id: {}", existing.getId());
            if (dto.getTripDetails().getDestination() != null) {
                if (dto.getTripDetails().getDestination().getName() != null) {
                    existing.setDestinationName(dto.getTripDetails().getDestination().getName());
                }
                if (dto.getTripDetails().getDestination().getCoordinates() != null) {
                    String coordStr = String.join(",",
                            dto.getTripDetails().getDestination().getCoordinates()
                                    .stream().map(Object::toString).toArray(String[]::new));
                    existing.setDestinationCoordinates(coordStr);
                }
            }
            if (dto.getTripDetails().getStartDate() != null) {
                existing.setStartDate(dto.getTripDetails().getStartDate());
            }
            if (dto.getTripDetails().getEndDate() != null) {
                existing.setEndDate(dto.getTripDetails().getEndDate());
            }
            if (dto.getTripDetails().getBudget() != null && dto.getTripDetails().getBudget().getBreakdown() != null) {
                log.debug("Updating budget details for Itinerary id: {}", existing.getId());
                if (existing.getBudget() == null) {
                    Budget budget = new Budget();
                    budget.setItinerary(existing);
                    existing.setBudget(budget);
                }
                if (dto.getTripDetails().getBudget().getCurrency() != null) {
                    existing.getBudget().setCurrency(dto.getTripDetails().getBudget().getCurrency());
                }
                existing.getBudget().setTotalAmount(BigDecimal.valueOf(dto.getTripDetails().getBudget().getTotal()));
                existing.getBudget().setAccommodationAmount(BigDecimal.valueOf(dto.getTripDetails().getBudget().getBreakdown().getAccommodation()));
                existing.getBudget().setActivitiesAmount(BigDecimal.valueOf(dto.getTripDetails().getBudget().getBreakdown().getActivities()));
                existing.getBudget().setDiningAmount(BigDecimal.valueOf(dto.getTripDetails().getBudget().getBreakdown().getDining()));
                existing.getBudget().setTransportAmount(BigDecimal.valueOf(dto.getTripDetails().getBudget().getBreakdown().getTransport()));
            }
        }

        // For days, here we choose to replace the entire collection.
        if (dto.getDays() != null) {
            log.debug("Replacing days for Itinerary id: {}", existing.getId());
            existing.getDays().clear();
            List<Day> updatedDays = dto.getDays().stream().map(dayDto -> {
                Day day = mapDaySectionsAndTravel(new Day(), dayDto);
                day.setItinerary(existing);
                return day;
            }).collect(Collectors.toList());
            existing.getDays().addAll(updatedDays);
        }

        // Replace sharedWith collection if provided
        if (dto.getSharedWith() != null) {
            log.debug("Replacing shared access for Itinerary id: {}", existing.getId());
            existing.getSharedAccess().clear();
            List<SharedAccess> shared = dto.getSharedWith().stream().map(s -> {
                SharedAccess sa = new SharedAccess();
                sa.setUserId(s.getUserId());
                sa.setEmail(s.getEmail());
                sa.setPermission(s.getPermission());
                sa.setCreatedAt(LocalDateTime.now());
                sa.setItinerary(existing);
                return sa;
            }).collect(Collectors.toList());
            existing.setSharedAccess(shared);
        }

        // Update metadata if provided
        if (dto.getMetadata() != null) {
            log.debug("Updating metadata for Itinerary id: {}", existing.getId());
            existing.setIsTemplate(dto.getMetadata().getIsTemplate());
            existing.setLanguage(dto.getMetadata().getLanguage());
            existing.setVersion(dto.getMetadata().getVersion());
        }
        log.info("Completed update of Itinerary id: {}", existing.getId());
    }

    // Helper: map a day (including sections and travel routes) from its DTO.
    private Day mapDaySectionsAndTravel(Day day, ItineraryDTO.DayDTO dayDto) {
        log.info("Mapping DayDTO to Day entity; DayDTO id: {}", dayDto.getId());
        if (dayDto.getId() != null) {
            day.setId(UUID.fromString(dayDto.getId()));
        }
        day.setDayNumber(dayDto.getDayNumber());
        day.setDate(dayDto.getDate());
        day.setNotes(dayDto.getNotes());
        if (dayDto.getBudget() != null) {
            day.setPlannedBudget(BigDecimal.valueOf(dayDto.getBudget().getPlanned()));
            day.setActualBudget(BigDecimal.valueOf(dayDto.getBudget().getActual()));
        }
        day.setCreatedAt(LocalDateTime.now());
        day.setUpdatedAt(LocalDateTime.now());

        // Map activities/sections
        List<Activity> allActivities = new ArrayList<>();
        if (dayDto.getSections() != null) {
            log.debug("Mapping sections for Day id: {}", day.getId());
            // Map hotels
            if (dayDto.getSections().getHotels() != null) {
                log.debug("Mapping hotels for Day id: {}", day.getId());
                dayDto.getSections().getHotels().forEach(hotelDto -> {
                    Activity act = new Activity();
                    act.setDay(day);
                    act.setSequenceNumber(1);

                    Place p = new Place();
                    p.setName(hotelDto.getTitle());
                    p.setDescription(hotelDto.getDescription());
                    if (hotelDto.getLocation() != null && hotelDto.getLocation().getCoordinates() != null) {
                        String coordStr = String.join(",", hotelDto.getLocation().getCoordinates()
                                .stream().map(Object::toString).toArray(String[]::new));
                        p.setCoordinates(coordStr);
                        p.setName(hotelDto.getLocation().getName());
                    }
                    p.setPlaceType("hotel");
                    if (hotelDto.getContact() != null) {
                        p.setPhone(hotelDto.getContact().getPhone());
                        p.setWebsite(hotelDto.getContact().getWebsite());
                        p.setGoogleMapsUrl(hotelDto.getContact().getGoogleMapsUrl());
                    }
                    if (hotelDto.getPhotos() != null) {
                        hotelDto.getPhotos().forEach(photoDto -> {
                            if (photoDto != null) {
                                Photo photo = new Photo();
                                photo.setUrl(photoDto.getUrl());
                                photo.setCaption(photoDto.getCaption());
                                photo.setCreatedAt(LocalDateTime.now());
                                photo.setPlace(p);
                                p.getPhotos().add(photo);
                            }
                        });
                    }
                    if (hotelDto.getOperatingHours() != null && hotelDto.getOperatingHours().getPeriods() != null) {
                        hotelDto.getOperatingHours().getPeriods().forEach(periodDto -> {
                            OperatingHours oh = new OperatingHours();
                            if (periodDto != null) {
                                oh.setDayOfWeek(periodDto.getDay());
                                if (periodDto.getHours() != null && periodDto.getHours().contains("-")) {
                                    String[] times = periodDto.getHours().split("-");
                                    try {
                                        oh.setOpenTime(LocalTime.parse(times[0].trim()));
                                        oh.setCloseTime(LocalTime.parse(times[1].trim()));
                                    } catch (Exception e) {
                                        log.error("Error parsing operating hours for hotel: {}", e.getMessage());
                                    }
                                } else {
                                    oh.setOpenTime(LocalTime.of(0, 0, 0));    // 00:00:00
                                    oh.setCloseTime(LocalTime.of(23, 59, 0));
                                }
                                oh.setPlace(p);
                                p.getOperatingHours().add(oh);
                            }
                        });
                    }
                    act.setPlace(p);
                    if (hotelDto.getStartTime() != null)
                        act.setStartTime(LocalTime.parse(hotelDto.getStartTime()));
                    if (hotelDto.getEndTime() != null)
                        act.setEndTime(LocalTime.parse(hotelDto.getEndTime()));
                    act.setDuration(hotelDto.getDuration());
                    act.setPrice(hotelDto.getPrice());
                    if (hotelDto.getBookingInfo() != null) {
                        BookingInfo bi = new BookingInfo();
                        bi.setConfirmationNumber(hotelDto.getBookingInfo().getConfirmationNumber());
                        bi.setReservationNumber(hotelDto.getBookingInfo().getReservationNumber());
                        bi.setNotes(hotelDto.getBookingInfo().getNotes());
                        act.setBookingInfo(bi);
                    }
                    act.setCreatedAt(LocalDateTime.now());
                    act.setUpdatedAt(LocalDateTime.now());
                    allActivities.add(act);
                });
            }

            // Map activities
            if (dayDto.getSections().getActivities() != null) {
                log.debug("Mapping activities for Day id: {}", day.getId());
                dayDto.getSections().getActivities().forEach(activityDto -> {
                    Activity act = new Activity();
                    act.setDay(day);
                    act.setSequenceNumber(2);
                    Place p = new Place();
                    p.setName(activityDto.getTitle());
                    p.setDescription(activityDto.getDescription());
                    if (activityDto.getLocation() != null && activityDto.getLocation().getCoordinates() != null) {
                        String coordStr = String.join(",", activityDto.getLocation().getCoordinates()
                                .stream().map(Object::toString).toArray(String[]::new));
                        p.setCoordinates(coordStr);
                        p.setName(activityDto.getLocation().getName());
                    }
                    p.setPlaceType("activity");
                    if (activityDto.getContact() != null) {
                        p.setPhone(activityDto.getContact().getPhone());
                        p.setWebsite(activityDto.getContact().getWebsite());
                        p.setGoogleMapsUrl(activityDto.getContact().getGoogleMapsUrl());
                    }
                    if (activityDto.getPhotos() != null) {
                        activityDto.getPhotos().forEach(photoDto -> {
                            if (photoDto != null) {
                                Photo photo = new Photo();
                                photo.setUrl(photoDto.getUrl());
                                photo.setCaption(photoDto.getCaption());
                                photo.setCreatedAt(LocalDateTime.now());
                                photo.setPlace(p);
                                p.getPhotos().add(photo);
                            }
                        });
                    }
                    if (activityDto.getOperatingHours() != null && activityDto.getOperatingHours().getPeriods() != null) {
                        activityDto.getOperatingHours().getPeriods().forEach(periodDto -> {
                            if (periodDto != null) {
                                OperatingHours oh = new OperatingHours();
                                oh.setDayOfWeek(periodDto.getDay());
                                if (periodDto.getHours() != null && periodDto.getHours().contains("-")) {
                                    String[] times = periodDto.getHours().split("-");
                                    try {
                                        oh.setOpenTime(LocalTime.parse(times[0].trim()));
                                        oh.setCloseTime(LocalTime.parse(times[1].trim()));
                                    } catch (Exception e) {
                                        log.error("Error parsing operating hours for activity: {}", e.getMessage());
                                    }
                                } else {
                                    oh.setOpenTime(LocalTime.of(0, 0, 0));
                                    oh.setCloseTime(LocalTime.of(23, 59, 0));
                                }
                                oh.setPlace(p);
                                p.getOperatingHours().add(oh);
                            }
                        });
                    }
                    act.setPlace(p);
                    if (activityDto.getStartTime() != null)
                        act.setStartTime(LocalTime.parse(activityDto.getStartTime()));
                    if (activityDto.getEndTime() != null)
                        act.setEndTime(LocalTime.parse(activityDto.getEndTime()));
                    act.setDuration(activityDto.getDuration());
                    act.setPrice(activityDto.getPrice());
                    if (activityDto.getBookingInfo() != null) {
                        BookingInfo bi = new BookingInfo();
                        bi.setConfirmationNumber(activityDto.getBookingInfo().getConfirmationNumber());
                        bi.setReservationNumber(activityDto.getBookingInfo().getReservationNumber());
                        bi.setNotes(activityDto.getBookingInfo().getNotes());
                        act.setBookingInfo(bi);
                    }
                    act.setCreatedAt(LocalDateTime.now());
                    act.setUpdatedAt(LocalDateTime.now());
                    allActivities.add(act);
                });
            }

            // Map restaurants
            if (dayDto.getSections().getRestaurants() != null) {
                log.debug("Mapping restaurants for Day id: {}", day.getId());
                dayDto.getSections().getRestaurants().forEach(restDto -> {
                    Activity act = new Activity();
                    act.setDay(day);
                    act.setSequenceNumber(3);
                    Place p = new Place();
                    p.setName(restDto.getTitle());
                    p.setDescription(restDto.getDescription());
                    if (restDto.getLocation() != null && restDto.getLocation().getCoordinates() != null) {
                        String coordStr = String.join(",", restDto.getLocation().getCoordinates()
                                .stream().map(Object::toString).toArray(String[]::new));
                        p.setCoordinates(coordStr);
                        p.setName(restDto.getLocation().getName());
                    }
                    p.setPlaceType("restaurant");
                    if (restDto.getContact() != null) {
                        p.setPhone(restDto.getContact().getPhone());
                        p.setWebsite(restDto.getContact().getWebsite());
                        p.setGoogleMapsUrl(restDto.getContact().getGoogleMapsUrl());
                    }
                    if (restDto.getPhotos() != null) {
                        restDto.getPhotos().forEach(photoDto -> {
                            if (photoDto != null) {
                                Photo photo = new Photo();
                                photo.setUrl(photoDto.getUrl());
                                photo.setCaption(photoDto.getCaption());
                                photo.setCreatedAt(LocalDateTime.now());
                                photo.setPlace(p);
                                p.getPhotos().add(photo);
                            }
                        });
                    }
                    if (restDto.getOperatingHours() != null && restDto.getOperatingHours().getPeriods() != null) {
                        restDto.getOperatingHours().getPeriods().forEach(periodDto -> {
                            OperatingHours oh = new OperatingHours();
                            if (periodDto != null) {
                                oh.setDayOfWeek(periodDto.getDay());
                                if (periodDto.getHours() != null && periodDto.getHours().contains("-")) {
                                    String[] times = periodDto.getHours().split("-");
                                    try {
                                        oh.setOpenTime(LocalTime.parse(times[0].trim()));
                                        oh.setCloseTime(LocalTime.parse(times[1].trim()));
                                    } catch (Exception e) {
                                        log.error("Error parsing operating hours for restaurant: {}", e.getMessage());
                                    }
                                } else {
                                    oh.setOpenTime(LocalTime.of(0, 0, 0));
                                    oh.setCloseTime(LocalTime.of(23, 59, 0));
                                }
                                oh.setPlace(p);
                                p.getOperatingHours().add(oh);
                            }
                        });
                    }
                    act.setPlace(p);
                    if (restDto.getStartTime() != null)
                        act.setStartTime(LocalTime.parse(restDto.getStartTime()));
                    if (restDto.getEndTime() != null)
                        act.setEndTime(LocalTime.parse(restDto.getEndTime()));
                    act.setDuration(restDto.getDuration());
                    act.setPrice(restDto.getPrice());
                    if (restDto.getBookingInfo() != null) {
                        BookingInfo bi = new BookingInfo();
                        bi.setConfirmationNumber(restDto.getBookingInfo().getConfirmationNumber());
                        bi.setReservationNumber(restDto.getBookingInfo().getReservationNumber());
                        bi.setNotes(restDto.getBookingInfo().getNotes());
                        act.setBookingInfo(bi);
                    }
                    act.setCreatedAt(LocalDateTime.now());
                    act.setUpdatedAt(LocalDateTime.now());
                    allActivities.add(act);
                });
            }
        }
        day.setActivities(allActivities);

        // Travel routes mapping commented out in original code.
        log.info("Completed mapping of DayDTO to Day entity; Day id: {}", day.getId());
        return day;
    }
}
