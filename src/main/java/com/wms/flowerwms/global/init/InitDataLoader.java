package com.wms.flowerwms.global.init;

import com.wms.flowerwms.inbound.dto.InboundCreateRequest;
import com.wms.flowerwms.inbound.service.InboundCommandService;
import com.wms.flowerwms.member.domain.Member;
import com.wms.flowerwms.member.domain.MemberRole;
import com.wms.flowerwms.member.domain.MemberStatus;
import com.wms.flowerwms.member.repository.MemberRepository;
import com.wms.flowerwms.outbound.dto.OutboundCreateRequest;
import com.wms.flowerwms.outbound.service.OutboundCommandService;
import com.wms.flowerwms.pallet.repository.PalletRepository;
import com.wms.flowerwms.product.domain.FlowerType;
import com.wms.flowerwms.product.dto.ProductCreateRequest;
import com.wms.flowerwms.product.repository.ProductRepository;
import com.wms.flowerwms.product.service.ProductCommandService;
import com.wms.flowerwms.section.domain.SectionType;
import com.wms.flowerwms.section.repository.SectionRepository;
import com.wms.flowerwms.warehouse.dto.WarehouseCreateRequest;
import com.wms.flowerwms.warehouse.repository.WarehouseRepository;
import com.wms.flowerwms.warehouse.service.WarehouseCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Profile({"local", "prod"})
@RequiredArgsConstructor
public class InitDataLoader implements ApplicationRunner {

    private final MemberRepository memberRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final SectionRepository sectionRepository;
    private final PalletRepository palletRepository;
    private final WarehouseCommandService warehouseCommandService;
    private final ProductCommandService productCommandService;
    private final InboundCommandService inboundCommandService;
    private final OutboundCommandService outboundCommandService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (memberRepository.existsByRole(MemberRole.ADMIN)) {
            log.info("초기 데이터가 이미 존재합니다. 스킵합니다.");
            return;
        }

        log.info("초기 데이터 로딩 시작...");

        // ADMIN 생성
        memberRepository.save(Member.builder()
                .loginId("admin")
                .password(passwordEncoder.encode("admin1234!"))
                .name("김윤성")
                .phone("01000000000")
                .email("admin@flowerwms.com")
                .role(MemberRole.ADMIN)
                .status(MemberStatus.ACTIVE)
                .build());

        // 2. 창고 3개 생성
        Long w1 = warehouseCommandService.createWarehouse(
                new WarehouseCreateRequest("서울 물류센터", "서울 강서구 마곡중앙로 161")
        );

        Long w2 = warehouseCommandService.createWarehouse(
                new WarehouseCreateRequest("경기 물류센터", "경기 고양시 덕양구 화신로 260")
        );

        Long w3 = warehouseCommandService.createWarehouse(
                new WarehouseCreateRequest("인천 물류센터", "인천 남동구 논현로 46")
        );

        // MANAGER 3명 생성 (각 창고 배정)
        var warehouse1 = warehouseRepository.findById(w1).get();
        var warehouse2 = warehouseRepository.findById(w2).get();
        var warehouse3 = warehouseRepository.findById(w3).get();

        memberRepository.save(Member.builder()
                .loginId("manager01")
                .password(passwordEncoder.encode("manager1234!"))
                .name("김영민")
                .phone("01011111111")
                .email("seoul@flowerwms.com")
                .role(MemberRole.MANAGER)
                .status(MemberStatus.ACTIVE)
                .warehouse(warehouse1)
                .build());

        memberRepository.save(Member.builder()
                .loginId("manager02")
                .password(passwordEncoder.encode("manager1234!"))
                .name("유종현")
                .phone("01022222222")
                .email("gyeonggi@flowerwms.com")
                .role(MemberRole.MANAGER)
                .status(MemberStatus.ACTIVE)
                .warehouse(warehouse2)
                .build());

        memberRepository.save(Member.builder()
                .loginId("manager03")
                .password(passwordEncoder.encode("manager1234!"))
                .name("윤희수")
                .phone("01033333333")
                .email("incheon@flowerwms.com")
                .role(MemberRole.MANAGER)
                .status(MemberStatus.ACTIVE)
                .warehouse(warehouse3)
                .build());

        // 상품 38개 생성
        // 절화 18개
        Long p1  = createProduct("장미 레드",       FlowerType.CUT, "단", 100, 15000);
        Long p2  = createProduct("장미 핑크",       FlowerType.CUT, "단", 100, 15000);
        Long p3  = createProduct("장미 화이트",     FlowerType.CUT, "단", 100, 15000);
        Long p4  = createProduct("장미 옐로우",     FlowerType.CUT, "단", 100, 14000);
        Long p5  = createProduct("튤립 레드",       FlowerType.CUT, "단", 100, 12000);
        Long p6  = createProduct("튤립 옐로우",     FlowerType.CUT, "단", 100, 12000);
        Long p7  = createProduct("튤립 퍼플",       FlowerType.CUT, "단", 100, 12000);
        Long p8  = createProduct("백합 화이트",     FlowerType.CUT, "단",  50, 20000);
        Long p9  = createProduct("백합 핑크",       FlowerType.CUT, "단",  50, 20000);
        Long p10 = createProduct("카네이션 레드",   FlowerType.CUT, "단", 100, 10000);
        Long p11 = createProduct("카네이션 핑크",   FlowerType.CUT, "단", 100, 10000);
        Long p12 = createProduct("거베라 레드",     FlowerType.CUT, "단", 100,  9000);
        Long p13 = createProduct("거베라 오렌지",   FlowerType.CUT, "단", 100,  9000);
        Long p14 = createProduct("안개꽃 화이트",   FlowerType.CUT, "단",  50,  8000);
        Long p15 = createProduct("국화 화이트",     FlowerType.CUT, "단", 100,  8000);
        Long p16 = createProduct("해바라기",        FlowerType.CUT, "단",  50, 11000);
        Long p17 = createProduct("리시안셔스 화이트", FlowerType.CUT, "단", 50, 13000);
        Long p18 = createProduct("리시안셔스 핑크", FlowerType.CUT, "단",  50, 13000);

        // 화분 10개
        Long p19 = createProduct("호접란 화분",    FlowerType.POTTED, "분",  6, 35000);
        Long p20 = createProduct("수국 화분",      FlowerType.POTTED, "분",  6, 25000);
        Long p21 = createProduct("라벤더 화분",    FlowerType.POTTED, "분",  8, 15000);
        Long p22 = createProduct("몬스테라 화분",  FlowerType.POTTED, "분",  4, 40000);
        Long p23 = createProduct("스투키 화분",    FlowerType.POTTED, "분",  6, 20000);
        Long p24 = createProduct("다육식물 화분",  FlowerType.POTTED, "분", 20,  8000);
        Long p25 = createProduct("장미 화분",      FlowerType.POTTED, "분",  6, 22000);
        Long p26 = createProduct("베고니아 화분",  FlowerType.POTTED, "분",  8, 12000);
        Long p27 = createProduct("고무나무 화분",  FlowerType.POTTED, "분",  4, 45000);
        Long p28 = createProduct("허브 화분",      FlowerType.POTTED, "분", 12, 10000);

        // 가공화 10개
        Long p29 = createProduct("프리저브드 장미 박스", FlowerType.PROCESSED, "개", 24, 30000);
        Long p30 = createProduct("프리저브드 장미 돔",   FlowerType.PROCESSED, "개", 12, 45000);
        Long p31 = createProduct("드라이플라워 꽃다발",  FlowerType.PROCESSED, "개", 20, 25000);
        Long p32 = createProduct("드라이플라워 리스",    FlowerType.PROCESSED, "개", 12, 35000);
        Long p33 = createProduct("비누꽃 장미 박스",     FlowerType.PROCESSED, "개", 24, 20000);
        Long p34 = createProduct("비누꽃 꽃다발",        FlowerType.PROCESSED, "개", 20, 18000);
        Long p35 = createProduct("조화 장미 꽃다발",     FlowerType.PROCESSED, "개", 30, 15000);
        Long p36 = createProduct("조화 튤립 꽃다발",     FlowerType.PROCESSED, "개", 30, 15000);
        Long p37 = createProduct("압화 액자",            FlowerType.PROCESSED, "개", 10, 50000);
        Long p38 = createProduct("압화 카드",            FlowerType.PROCESSED, "개", 50,  5000);

        // 입고 데이터 생성
        createInbounds(w1, w2, w3,
                List.of(p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,
                        p11,p12,p13,p14,p15,p16,p17,p18,
                        p19,p20,p21,p22,p23,p24,p25,p26,p27,p28,
                        p29,p30,p31,p32,p33,p34,p35,p36,p37,p38));

        // 출고 데이터 생성
        createOutbounds(w1, w2, w3,
                List.of(p1,p2,p5,p10,p12,p19,p20,p29,p31,p33));

        log.info("초기 데이터 로딩 완료!");
    }

    private Long createProduct(String name, FlowerType type, String unit, int qtyPerBox, int pricePerBox) {
        return productCommandService.createProduct(ProductCreateRequest.builder()
                .name(name)
                .type(type)
                .unit(unit)
                .qtyPerBox(qtyPerBox)
                .pricePerBox(pricePerBox)
                .build());
    }

    private void createInbounds(Long w1, Long w2, Long w3, List<Long> productIds) {
        // 각 창고의 COLD, NORMAL 섹션 조회
        var w1Sections = sectionRepository.findByWarehouseId(w1);
        var w2Sections = sectionRepository.findByWarehouseId(w2);
        var w3Sections = sectionRepository.findByWarehouseId(w3);

        // 절화(COLD) 상품 - 창고별 입고
        List<Long> coldProducts = productIds.subList(0, 18); // 절화 18개
        List<Long> normalProducts = productIds.subList(18, productIds.size()); // 화분+가공화 20개

        // 서울 창고 입고
        inboundForWarehouse(w1, w1Sections, coldProducts, normalProducts);
        // 경기 창고 입고
        inboundForWarehouse(w2, w2Sections, coldProducts, normalProducts);
        // 인천 창고 입고
        inboundForWarehouse(w3, w3Sections, coldProducts, normalProducts);
    }

    private void inboundForWarehouse(Long warehouseId, List<?> sections, List<Long> coldProducts, List<Long> normalProducts) {
        var coldSection = sectionRepository.findByWarehouseId(warehouseId).stream()
                .filter(s -> s.getType() == SectionType.COLD).findFirst().get();
        var normalSection = sectionRepository.findByWarehouseId(warehouseId).stream()
                .filter(s -> s.getType() == SectionType.NORMAL).findFirst().get();

        var coldPallets = palletRepository.findBySectionId(coldSection.getId());
        var normalPallets = palletRepository.findBySectionId(normalSection.getId());

        int palletIdx = 0;

        // 절화 입고 (COLD)
        for (int i = 0; i < coldProducts.size(); i++) {
            inboundCommandService.createInbound(InboundCreateRequest.builder()
                    .warehouseId(warehouseId)
                    .sectionId(coldSection.getId())
                    .palletId(coldPallets.get(palletIdx % coldPallets.size()).getId())
                    .productId(coldProducts.get(i))
                    .boxQty(3)
                    .build());
            palletIdx++;
        }

        palletIdx = 0;

        // 화분+가공화 입고 (NORMAL)
        for (int i = 0; i < normalProducts.size(); i++) {
            inboundCommandService.createInbound(InboundCreateRequest.builder()
                    .warehouseId(warehouseId)
                    .sectionId(normalSection.getId())
                    .palletId(normalPallets.get(palletIdx % normalPallets.size()).getId())
                    .productId(normalProducts.get(i))
                    .boxQty(3)
                    .build());
            palletIdx++;
        }
    }

    private void createOutbounds(Long w1, Long w2, Long w3, List<Long> productIds) {
        // 각 창고에서 일부 상품 출고
        for (Long productId : productIds) {
            try {
                outboundCommandService.createOutbound(OutboundCreateRequest.builder()
                        .warehouseId(w1)
                        .productId(productId)
                        .boxQty(1)
                        .build());
            } catch (Exception e) {
                log.warn("출고 실패 (w1): productId={}, {}", productId, e.getMessage());
            }
            try {
                outboundCommandService.createOutbound(OutboundCreateRequest.builder()
                        .warehouseId(w2)
                        .productId(productId)
                        .boxQty(1)
                        .build());
            } catch (Exception e) {
                log.warn("출고 실패 (w2): productId={}, {}", productId, e.getMessage());
            }
        }
    }
}