package com.example.loancalculator.view;

import com.example.loancalculator.entity.Payment;
import com.example.loancalculator.entity.PaymentSchedule;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.UserError;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.FooterRow;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.TextRenderer;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.DoubleSummaryStatistics;

@SpringUI
public class MyUI extends UI {
    @Value("${app.minValue}")
    private Integer MIN_VALUE;
    @Value("${app.maxValue}")
    private Integer MAX_VALUE;
    @Value("${app.minPeriod}")
    private Integer MIN_PERIOD;
    @Value("${app.maxPeriod}")
    private Integer MAX_PERIOD;
    @Value("${app.percent}")
    private double percent;

    TextField textFieldPeriod;
    TextField textFieldSumm;
    Label labelPayment;
    PaymentSchedule paymentSchedule;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        paymentSchedule = new PaymentSchedule(MIN_VALUE, MIN_PERIOD, percent);
        final VerticalLayout baseLayout = new VerticalLayout();
        Button button = new Button("График платежей", i -> getDialog());
        button.setIcon(VaadinIcons.ARROW_FORWARD);
        baseLayout.setMargin(true);
        setContent(baseLayout);
        addFieldSumm(baseLayout);
        addPeriod(baseLayout);
        baseLayout.addComponent(button);
    }

    private void addFieldSumm(Layout layout){
        final Slider slider = new Slider(MIN_VALUE, MAX_VALUE);
        slider.setWidth("50%");
        final Label labelPersent = new Label();
        labelPersent.setValue("Ставка: " + percent + "%");
        textFieldSumm = new TextField("Размер кредита:");
        textFieldSumm.setValue(String.valueOf(MIN_VALUE));
        labelPayment = new Label();
        labelPayment.setValue("Ежемесячный платеж: ");
        checkValue(slider, textFieldSumm);
        slider.addValueChangeListener(event -> textFieldSumm.setValue(String.valueOf(event.getValue().intValue())));
        layout.addComponents(labelPersent, labelPayment, textFieldSumm, slider);
    }

    private void addPeriod(Layout layout){
        final Slider slider = new Slider(MIN_PERIOD, MAX_PERIOD);
        slider.setWidth("50%");
        textFieldPeriod = new TextField("Период:");
        textFieldPeriod.setValue(String.valueOf(MIN_PERIOD));
        checkValue(slider, textFieldPeriod);
        slider.addValueChangeListener(event -> textFieldPeriod.setValue(String.valueOf(event.getValue().intValue())));
        layout.addComponents(textFieldPeriod, slider);
    }

    private void checkValue(Slider slider, TextField textField) {
        textField.addValueChangeListener(event -> {
            try {
                slider.setValue(Double.valueOf(event.getValue()));
                textField.setComponentError(null);
                labelPayment.setValue("Ежемесячный платеж: " + getPayment(Integer.valueOf(textFieldSumm.getValue()),
                        Integer.valueOf(textFieldPeriod.getValue()),
                        percent));
            }catch (Slider.ValueOutOfBoundsException e){
                textField.setComponentError(new UserError("Не верное число!"));
            }catch (NumberFormatException e){
                textField.setComponentError(new UserError("Не число!"));
            }
        });
    }

    private BigDecimal getPayment(int summ, int year, double percent){
        paymentSchedule.setSumm(summ);
        paymentSchedule.setYear(year);
        paymentSchedule.setPercent(percent);
        return new BigDecimal(paymentSchedule.getMonthlyPayment()).setScale(2, BigDecimal.ROUND_HALF_DOWN);
    }

    private void getDialog(){
        final Window window = new Window("График платежей");
        window.setClosable(true);
        window.setModal(true);
        window.setDraggable(false);
        final VerticalLayout content = new VerticalLayout();
        content.setMargin(true);

        window.setContent(content);
        DecimalFormat df = new DecimalFormat("0.00");
        Grid<Payment> grid = new Grid<>();
        grid.setItems(paymentSchedule.getPayments());
        grid.setFooterVisible(true);
        grid.setWidth("740px");
        grid.setHeaderRowHeight(80);
        FooterRow footer = grid.appendFooterRow();
        grid.addColumn(Payment::getDate).setCaption("Дата платежа").setWidth(120);
        grid.addColumn(Payment::getDebtOnTheLoan).setCaption("Задолженность по кредиту").setWidth(150);
        grid.addColumn(Payment::getInterestPayment).setCaption("Платеж по процентам").setId("percentColumn").setWidth(150);
        grid.addColumn(Payment::getCreditPayment).setCaption("Платеж по кредиту").setId("creditColumn").setWidth(150);
        grid.addColumn(Payment::getTotalPayment).setCaption("Общий платеж").setId("fullColumn").setWidth(150);
        footer.getCell("percentColumn").setText(df.format(paymentSchedule.getTotalInterestPayment()));
        footer.getCell("creditColumn").setText(df.format(paymentSchedule.getTotalDebtPayment()));
        footer.getCell("fullColumn").setText(df.format(paymentSchedule.getSumm() + paymentSchedule.getTotalInterestPayment()));
        content.addComponent(grid);
        addWindow(window);
    }

}
