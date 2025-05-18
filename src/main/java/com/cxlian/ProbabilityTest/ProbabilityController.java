// 文件：src/main/java/com/cxlian/ProbabilityTest/ProbabilityController.java
package com.cxlian.ProbabilityTest;

import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // 允许所有跨域请求
public class ProbabilityController {

    public static int rd() {
        return ThreadLocalRandom.current()
                .nextInt(1, 1000 + 1);
    }

    // POST请求处理示例
    @PostMapping("/mode_1")
    public CalculationResult calculateProbability_1(@RequestBody InputParams params) {
        // 参数验证
        if (!Objects.equals(params.check(), "OK")) {
            return new CalculationResult(params.check());
        }

        // 调用核心计算方法
        String result = calculate_1(
                params.getNow_chou(),
                params.getChr_chou(),
                params.getWea_chou(),
                params.getChr_have(),
                params.getWea_have(),
                params.getHave_big()
        );

        return new CalculationResult(result);
    }

    @PostMapping("/mode_2")
    public CalculationResult calculateProbability_2(@RequestBody InputParams params) {
        // 参数验证
        if (!Objects.equals(params.check(), "OK")) {
            return new CalculationResult(params.check());
        }

        // 调用核心计算方法
        String result = calculate_2(
                params.getNow_chou(),
                params.getChr_chou(),
                params.getWea_chou(),
                params.getChr_have(),
                params.getWea_have(),
                params.getHave_big()
        );

        return new CalculationResult(result);
    }

    @PostMapping("/mode_3")
    public CalculationResult calculateProbability_3(
            @RequestBody InputParams params,
            @RequestParam(defaultValue = "0") int need) {
        // 参数验证
        if (!Objects.equals(params.check(), "OK")) {
            return new CalculationResult(params.check());
        }
        if (need < 0 || need > 6) {
            return new CalculationResult("只能获取 0-6 链！");
        }

        // 调用核心计算方法
        String result = calculate_3(
                params.getNow_chou(),
                params.getChr_chou(),
                params.getWea_chou(),
                params.getChr_have(),
                params.getWea_have(),
                params.getHave_big(),
                need
        );

        return new CalculationResult(result);
    }

    // 不抽武器
    private String calculate_1(int now_chou, int chr_chou, int wea_chou, int chr_have, int wea_have, boolean have_big) {
//        long startTime = System.currentTimeMillis();

        int[] c = new int[31];
        for (int i = 0; i <= 30; i++) c[i] = 0;
        for (int j = 1; j <= Constants.count; j++) {
            int ans = 0;
            int now = chr_have + 1;
            boolean flag = have_big;
            for (int i = 1; i <= now_chou + chr_chou; i++) {
                int tmp = rd();
                if (tmp <= Constants.get(now)) {
                    if (!flag && rd() <= 500) {
                        flag = true;
                    } else {
                        ans++;
                        flag = false;
                    }
                    now = 1;
                } else now++;
            }
            if (ans <= 30) c[ans]++;
            else c[30]++;
        }
        String res = "";
        for (int i = 1; i <= 7; i++) {
            int sum = 0;
            for (int j = 30; j >= i; j--) sum += c[j];
            res = res + "" + (i - 1) + "+" + 0 + " 的概率是: " + String.format("%.4f", 1.0 * sum / Constants.count) + "\n";
        }

        return res;
//        long endTime = System.currentTimeMillis();
//        return res+"\n执行耗时：" + (endTime - startTime) + "ms";
    }

    //一把武器
    private String calculate_2(int now_chou, int chr_chou, int wea_chou, int chr_have, int wea_have, boolean have_big) {
        int[] c = new int[31];
        for (int i = 0; i <= 30; i++) c[i] = 0;
        for (int j = 1; j <= Constants.count; j++) {
            boolean have_wea = false;
            int now = wea_have + 1;
            for (int i = 1; i <= wea_chou; i++) {
                int tmp = rd();
                if (tmp <= Constants.get(now)) {
                    have_wea = true;
                    break;
                } else now++;
            }
            int chou_shu = now_chou;
            if (!have_wea) {
                for (int i = 1; i <= now_chou; i++) {
                    chou_shu--;
                    int tmp = rd();
                    if (tmp <= Constants.get(now)) {
                        have_wea = true;
                        break;
                    } else {
                        now++;
                    }
                }
            }
            if (!have_wea) continue;

            int ans = 0;
            now = chr_have + 1;
            boolean flag = have_big;
            for (int i = 1; i <= chou_shu + chr_chou; i++) {
                int tmp = rd();
                if (tmp <= Constants.get(now)) {
                    if (!flag && rd() <= 500) {
                        flag = true;
                    } else {
                        ans++;
                        flag = false;
                    }
                    now = 1;
                } else now++;
            }
            if (ans <= 30) c[ans]++;
            else c[30]++;
        }
        String res = "";
        for (int i = 1; i <= 7; i++) {
            int sum = 0;
            for (int j = 30; j >= i; j--) sum += c[j];
            res = res + "" + (i - 1) + "+" + 1 + " 的概率是: " + String.format("%.4f", 1.0 * sum / Constants.count) + "\n";
        }
        return res;
    }

    //先抽need个命座
    private String calculate_3(int now_chou, int chr_chou, int wea_chou, int chr_have, int wea_have, boolean have_big, int need) {
        int[] c = new int[31];
        for (int i = 0; i <= 30; i++) c[i] = 0;
        for (int j = 1; j <= Constants.count; j++) {
            int have_chr = 0;
            int now = chr_have + 1;
            boolean flag = have_big;//是否大保底
            for (int i = 1; i <= chr_chou; i++) {
                int tmp = rd();
                if (tmp <= Constants.get(now)) {
                    if (!flag && rd() <= 500) {
                        flag = true;
                    } else {
                        have_chr++;
                        flag = false;
                    }
                    now = 1;
                } else now++;
                if (have_chr == need + 1) break;
            }
            int chou_shu = now_chou;
            if (have_chr != need + 1) {
                for (int i = 1; i <= now_chou; i++) {
                    chou_shu--;
                    int tmp = rd();
                    if (tmp <= Constants.get(now)) {
                        if (!flag && rd() <= 500) {
                            flag = true;
                        } else {
                            have_chr++;
                            flag = false;
                        }
                        now = 1;
                    } else {
                        now++;
                    }
                    if (have_chr == need + 1) break;
                }
            }
            if (have_chr != need + 1) continue;

            int ans = 0;
            now = wea_have + 1;
            for (int i = 1; i <= chou_shu + wea_chou; i++) {
                int tmp = rd();
                if (tmp <= Constants.get(now)) {
                    ans++;
                    now = 1;
                } else now++;
            }
            if (ans <= 30) c[ans]++;
            else c[30]++;
        }
        String res = "";
        for (int i = 0; i <= 5; i++) {
            int sum = 0;
            for (int j = 30; j >= i; j--) sum += c[j];
            res = res + "" + need + "+" + i + " 的概率是: " + String.format("%.4f", 1.0 * sum / Constants.count) + "\n";
        }
        return res;
    }

    private long combination(int n, int k) {
        if (k > n) return 0;
        if (k == 0 || k == n) return 1;
        return combination(n - 1, k - 1) + combination(n - 1, k);
    }

    // 定义请求响应DTO
    @Data
    public static class InputParams {
        private int now_chou; //抽数
        private int chr_chou; //角色抽
        private int wea_chou; //武器抽
        private int chr_have; //角色池垫的
        private int wea_have; //武器池垫的
        private boolean have_big; //有无大保底

        InputParams(int now_chou, int chr_chou, int wea_chou, int chr_have, int wea_have, boolean have_big) {
            this.now_chou = now_chou / 160;
            this.chr_chou = chr_chou;
            this.wea_chou = wea_chou;
            this.chr_have = chr_have;
            this.wea_have = wea_have;
            this.have_big = have_big;
        }

        String check() {
            if (now_chou < 0) return "星声数不能小于 0 ！";
            if (chr_chou < 0) return "角色抽数不能小于 0 ！";
            if (wea_chou < 0) return "武器抽数不能小于 0 ！";
            if (chr_have < 0 || chr_have > 80) return "已垫角色池数不能小于 0 或者大于 80 ！";
            if (wea_have < 0 || wea_have > 80) return "已垫武器池数不能小于 0 或者大于 80 ！";
            return "OK";
        }

        boolean getHave_big() {
            return have_big;
        }
    }

    @Data
    public static class CalculationResult {
        private final String result;

        public CalculationResult(String result) {
            this.result = result;
        }

        // getter
    }
}