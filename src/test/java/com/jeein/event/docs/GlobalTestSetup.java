// package com.jeein.event.docs;
//
// import com.jeein.event.dto.feign.JoinRequestDTO;
// import com.jeein.event.feign.MemberServiceFeignClient;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;
// import org.springframework.test.context.TestContext;
// import org.springframework.test.context.TestExecutionListener;
// import org.springframework.test.context.TestExecutionListeners;
//
// @Component
// @TestExecutionListeners(
// listeners = GlobalTestSetup.class,
// mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
// )
// public class GlobalTestSetup implements TestExecutionListener {
//
// @Autowired
// MemberServiceFeignClient memberServiceFeignClient;
// private static boolean initialized = false;
// public static String managerId;
//
// @Override
// public void beforeTestClass(TestContext testContext) {
// if (!initialized) {
// System.out.println("전체 테스트 시작 전 한 번 실행");
// initialized = true;
//
// managerId =
// memberServiceFeignClient.joinManager(
// JoinRequestDTO.of("testmanager@example.com", "매니저 이름", "매니저 닉네임", "12345678"))
// .getBody().getData().getId(); }
// }
//
// @Override
// public void afterTestClass(TestContext testContext) {
// System.out.println("전체 테스트 끝난 후 딱 한 번 실행");
//
// memberServiceFeignClient.hardDeleteManager(managerId);
// }
// }
