package yuanh.himes.avtivi.listener;

import java.util.Arrays;
import java.util.Optional;

/**
 * @Author: guochen
 * @Description:
 * @Date:Created in 1:18 PM 7/3/2020
 */
public enum ApproveResultEnum {
        REFUSE(0, "退件"),
        RETURN(1, "回退"),
        AGREE(2, "同意"),
        JUMP(3, "跳转");

        private Integer id;
        private String code;

        ApproveResultEnum(Integer id, String code) {
            this.id = id;
            this.code = code;
        }

        public Integer getId() {
            return id;
        }

        public String getCode() {
            return code;
        }

        static public ApproveResultEnum codeOf(String code) {
            Optional<ApproveResultEnum> filterResult = Arrays.stream(ApproveResultEnum.values()).filter(item -> item.code.equals(code)).findAny();
            return filterResult.orElse(null);
        }

        static public ApproveResultEnum idOf(Integer id) {
            Optional<ApproveResultEnum> filterResult = Arrays.stream(ApproveResultEnum.values()).filter(item -> item.id.equals(id)).findAny();
            return filterResult.orElse(null);
        }
}
