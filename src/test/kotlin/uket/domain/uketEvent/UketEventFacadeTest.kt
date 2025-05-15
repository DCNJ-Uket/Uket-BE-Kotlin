package uket.domain.uketEvent

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec

class UketEventFacadeTest :
    DescribeSpec({

        isolationMode = IsolationMode.InstancePerLeaf

//        it("더미데이터 출력") {
//            UketEventRandomUtil.createDummyData(100)
//        }
    }) {
    companion object {
    }
}
