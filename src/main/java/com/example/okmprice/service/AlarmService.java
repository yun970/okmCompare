package com.example.okmprice.service;

import com.example.okmprice.DTO.AlarmDto;
import com.example.okmprice.model.Alarm;
import com.example.okmprice.model.Price;
import com.example.okmprice.repository.AlarmRepository;
import com.example.okmprice.repository.PriceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final PriceRepository priceRepository;
    private final MailService mailService;

    public AlarmService(AlarmRepository alarmRepository, PriceRepository priceRepository, MailService mailService) {
        this.alarmRepository = alarmRepository;
        this.priceRepository = priceRepository;
        this.mailService = mailService;
    }

    public Alarm alarmRegistry(AlarmDto alarmDto){
        Alarm alarm = new Alarm();
        alarm.setEmail(alarmDto.getEmail());
        alarm.setProductNum(alarmDto.getProductNum());
        alarm.setPrice(alarmDto.getPrice());
        return alarmRepository.save(alarm);
    }

    public void alarmMailSend() {
        List<Alarm> lst = alarmRepository.findAll();
        for(Alarm iter : lst){
            if(iter.getPrice().getProductPrice() < priceRepository.findFirstByProductNumOrderByCreateDateDesc(iter.getProductNum()).getProductPrice()){
                System.out.printf("현재 가격"+iter.getPrice().getProductPrice()+"최저가격"+priceRepository.findFirstByProductNumOrderByCreateDateDesc(iter.getProductNum()).getProductPrice());
                mailService.sendMail(iter.getEmail());
                System.out.printf("메일발송됨");
            }
        }
//        lst.stream().filter(alarm -> alarm.getPrice().getProductPrice() < (priceRepository.findLatestPriceByProductNumber(alarm.getProductNum()).getProductPrice()))
//                .forEach(alarm -> mailService.sendMail(alarm.getEmail()));
    }



//  알람 테이블에서 모든 알람 확인 -> 각 행에서 Price의 가격을 추출하고, 해당 price의 가격과 오늘 price 가격을 비교해서 금일 가격이 더 싸다면 mail 발송

}
