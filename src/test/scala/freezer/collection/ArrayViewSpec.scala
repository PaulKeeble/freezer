package freezer.collection
import org.junit.runner.RunWith
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ArrayViewSpec extends FunSpec with ShouldMatchers {
  val arr = Array(1,2,3)
  describe("An ArrayView") {
    it("should read from an underlying array") {
      val view = new ArrayView(arr)
      view(0) should equal(1)
      view(1) should equal(2)
      view(2) should equal(3)
    }
    
    it("should read the length from the underlying array") {
      val view = new ArrayView(arr)
      view.length should equal(3)
    }
    
    it("drop should produce an immutable view that shifts its access") {
      val view = new ArrayView(arr)
      val newView = view.drop(1)
      newView.length should equal(2)
      newView(0) should equal(2)
      newView(1) should equal(3)
    }
    
    it("drop should not change the underlying array or the original view") {
      val view = new ArrayView(arr)
      val newView = view.drop(1)
      arr should equal(Array(1,2,3))
      view(0) should equal(1)
    }
    
    it("take should produce view of just the front of the array") {
      val view = new ArrayView(arr)
      val newView = view.take(1)
      newView.length should equal(1)
      newView(0) should equal(1)
    }
    
    it("split at 0 should produce an empty and full view") {
      val view = new ArrayView(arr)
      val (empty,all) = view.splitAt(0)
      empty.length should equal(0)
      all.length should equal(3)
    }
    
    it("split at mid should produce two partial arrays") {
      val view = new ArrayView(arr)
      val (one,twoThree) = view.splitAt(1)
      one.length should equal(1)
      one(0) should equal(1)
      twoThree.length should equal(2)
      twoThree(0) should equal(2)
      twoThree(1) should equal(3)
    }
    
    it("should allow multiple operations, each updating the last") {
      val view = new ArrayView(Array(1,2,3,4))
      val middle = view.take(3).drop(1)
      middle.length should equal(2)
      middle(0) should equal(2)
      middle(1) should equal(3)
    }
    
    it("toArray should produce a copy of the views array data") {
      val view = new ArrayView(Array(1,2,3,4))
      val middle = view.take(3).drop(1)
      middle.toArray should equal(Array(2,3))
    }
    
    it("implicits should convert to and from arrays") {
      import ArrayView._
      def dropOne[T](arr:ArrayView[T]) = arr.drop(1)
      
      dropOne(Array(1,2,3)) === Array(2,3)
    }
  }
}