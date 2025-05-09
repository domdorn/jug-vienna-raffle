package com.vaadin.demo.application.views.admin.details;

import com.vaadin.demo.application.services.RaffleService;
import com.vaadin.demo.application.services.meetup.MeetupService;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.firitin.components.RichText;

import java.time.format.DateTimeFormatter;

@Route(value = "details", layout = DetailsMainLayout.class)
public class DetailsSubView extends VerticalLayout implements BeforeEnterObserver {

    private final RichText descriptionTextArea = new RichText();
    private final TextField titleField = new TextField("Title");
    private final TextField dateTimeField = new TextField("Datetime");
    private final TextField tokenField = new TextField("Token");
    private final RaffleService raffleService;
    private final MeetupService meetupService;

    public DetailsSubView(RaffleService raffleService, MeetupService meetupService) {
        this.raffleService = raffleService;
        this.meetupService = meetupService;

        var descriptionTitle = new Span("Description");
        descriptionTitle.addClassNames(LumoUtility.FontWeight.NORMAL, LumoUtility.FontSize.SMALL,
                LumoUtility.TextColor.BODY);
        var scroller = new Scroller(descriptionTextArea);
        scroller.addClassNames(LumoUtility.Border.ALL,
                LumoUtility.BorderColor.CONTRAST_30,
                LumoUtility.BorderRadius.MEDIUM,
                LumoUtility.Padding.MEDIUM);

        titleField.setReadOnly(true);
        titleField.setWidth(100, Unit.PERCENTAGE);
        add(titleField);

        dateTimeField.setReadOnly(true);
        tokenField.setReadOnly(true);

        var componentLayout = new HorizontalLayout();
        componentLayout.add(dateTimeField, tokenField);
        componentLayout.setPadding(false);

        add(componentLayout, descriptionTitle);
        addAndExpand(scroller);
        setJustifyContentMode(JustifyContentMode.START);

        setPadding(false);
    }

    private void updateContent(MeetupService.MeetupEvent event) {
        titleField.setValue(event.title());
        dateTimeField.setValue(event.dateTime().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        descriptionTextArea.withMarkDown(event.description());
        tokenField.setValue(event.token());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Optional: Zugriff auf Route-Parameter, wenn benötigt
        RouteParameters parameters = event.getRouteParameters();
        parameters.get(DetailsMainLayout.RAFFLE_ID_PARAMETER)
                .flatMap(raffleId -> raffleService.get(Long.parseLong(raffleId)))
                .flatMap(raffle -> meetupService.getEvent(raffle.getMeetup_event_id()))
                .ifPresent(this::updateContent);
    }
}
