package freezer.obj
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.BeforeAndAfter
import org.scalatest.FunSpec
import scala.collection.mutable.LinkedHashSet

@RunWith(classOf[JUnitRunner])
class ObjectIndexSpec extends FunSpec with ShouldMatchers with BeforeAndAfter {
  val set = new ObjectIndex

  describe("An ObjectIndex") {
    it("should see different objects even if equals/hashcode are implemented") {
      val o1 = new Object { override def hashCode = 1; override def equals(a: Any) = true }
      val o2 = new Object { override def hashCode = 1; override def equals(a: Any) = true }
      set += o1
      set += o2

      set.indexOf(o1) should equal(Some(0))
      set.indexOf(o2) should equal(Some(1))
    }

    it("should be equivalent") {
      val o1 = new Object
      val o2 = new Object

      val index1 = new ObjectIndex += o1
      val index2 = new ObjectIndex += o2

      assert(index1 equivalent (index2))
    }

    it("should not be equivalent as classes differ") {
      val o1 = new Object
      val o2 = Array()

      val index1 = new ObjectIndex += o1
      val index2 = new ObjectIndex += o2

      assert(!(index1 equivalent (index2)))
    }
    
    it("should have an iterator over the objects") {
      val o1 = new Object
      val o2 = new Object
      set +=o1 +=o2
      
      LinkedHashSet(o1,o2) equals (set.objs)
    }
  }
}