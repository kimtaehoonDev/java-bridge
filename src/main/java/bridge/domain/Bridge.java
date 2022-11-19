package bridge.domain;

import java.util.List;
import java.util.stream.Collectors;

public class Bridge {
    private List<FootrestLocation> structure;

    public Bridge(List<String> structure) {
        if (structure.size() < 3 || 20 < structure.size()) {
            throw new IllegalArgumentException("3 이상, 20 이하의 크기로만 입력이 가능합니다");
        }
        this.structure = structure.stream().map(each ->
                        FootrestLocation.findByUserInput(each))
                .collect(Collectors.toList());
        System.out.println("bridge is " + structure); //TODO 마지막에 해당 문장 삭제. 테스트의 편의를 위해서 존재함
    }

    public boolean canMove(Integer order, FootrestLocation footrestLocation) {
        //order 크기 검증
        if (structure.size() <= order || order < 0) {
            throw new IllegalStateException("다리 인덱스를 벗어남");
        }
        return structure.get(order) == footrestLocation;
    }

    public boolean isLast(Integer order) {
        return structure.size() - 1 == order;
    }

    public Integer size() {
        return structure.size();
    }
}
