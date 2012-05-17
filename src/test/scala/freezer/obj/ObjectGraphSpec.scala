package freezer.obj
import org.junit.runner.RunWith
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ObjectGraphSpec extends FunSpec with ShouldMatchers {
  describe("An object graph") {
    it("should contain the root when no object fields and no primitive fields") {
      val root = new Object

      allInGraph(root) should equal (List(root))
    }
    
    it("should contain only the root with only primitive fields") {
      val root = new Object {
        val z : Boolean = true
        val b : Byte = 1
        val sh: Short = 1
        val i : Int = 1
        val l : Long = 1
        val f : Float = 1.0f
        val d : Double = 1.0
        val c : Char = 1
        
        override def toString="root"
      }

      allInGraph(root) should equal (List(root))
    }
    
    it("should contain only the root with String fields, treat Strings as primitive") {
      val root = new Object { 
        val s = "a string"
      }
      
      allInGraph(root) should equal (List(root))
    }
    
    it("should contain an object when there is an AnyRef field") {
      val inner = new Object {
        override def toString="inner"
      }
      val root = new Object {
        val o = inner
        
        override def toString="root"
      }
      
      allInGraph(root) should equal (List(root,inner))
    }
    
    
    it("should contain all objects when graph is deeper") {
      val inner2 = new Object {
        override def toString = "inner2"
      }
      val inner = new Object {
        val o = inner2
        override def toString = "inner"
      }
      val root = new Object {
        val o = inner
        override def toString = "root"
      }
      
      allInGraph(root) should equal (List(root,inner,inner2))
    }
    
    it("should support very deep graphs (>1024)") {
      var current = new AClass(null,0)
      
      1.to(9999).foreach { i =>
        current = new AClass(current,i)
      }
      
      allInGraph(current) should have size (10000)
    }
  }
  
  def allInGraph(root : AnyRef) = {
    val graph = new ObjectGraph(root)
    graph.allObjects
  }
}

class AClass(val a: AClass, val i: Int) {

  override def toString = i.toString()
}