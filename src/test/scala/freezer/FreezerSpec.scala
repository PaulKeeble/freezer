package freezer

import org.scalatest.FeatureSpec
import org.junit.runner.RunWith
import org.scalatest.GivenWhenThen
import org.scalatest.junit.JUnitRunner
import scala.util.control.TailCalls.TailRec
import scala.annotation.tailrec
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class FreezerSpec extends FeatureSpec with GivenWhenThen with ShouldMatchers {
  val freezer = new Freezer
  feature("Freezing null") {
    scenario("null") {
      roundTrip(null)
    }
  }
  
  feature("Freezing primitive fields") {
    scenario("byte") {
      roundTrip(new ByteObject(1))
    }
    
    scenario("int") {
      roundTrip(new IntObject(1))
    }
    
    scenario("long") {
      roundTrip(new LongObject(1))
    }
    
    scenario("short") {
      roundTrip(new ShortObject(1))
    }
    
    scenario("float") {
      roundTrip(new FloatObject(1.1f))
    }
    
    scenario("double") {
      roundTrip(new DoubleObject(1.1d))
    }
    
    scenario("char") {
      roundTrip(new CharObject('c'))
    }

    scenario("multiple primitives") {
      roundTrip(new MultiplePrimitivesObject(b=1,s=2,i=3,l=4,f=1.1f,d=2.2d,c='g'))
    }
  }

  feature("Freezing Arrays") {
  	//store an array as a field from an object
  
  	//store an array as primary object
  
    //array with primitives

    //2 dimension array

    //3 dimension array

    //multidimension array with different lengths using Objects
  }
  
  feature("Freezing object graphs") {
     scenario("null object reference") {
      roundTrip(new ObjectObject(null))
    }
    
    scenario("nested object") {
      roundTrip(new ObjectObject(new IntObject(2)))
    }
    
    scenario("same object type of object nested") {
      roundTrip(new DeepObject(new DeepObject(null)))
    }
    
    scenario("multiple object fields") {
      roundTrip(new MultiObject(new IntObject(1), new IntObject(2)))
    }
    
    scenario("deep graph") {
      given("A deep object graph")
      val end = new DeepObject()
      var current = end
      
      1 to 9999 foreach { _=>
        current = new DeepObject(current)
      }
      val result = roundTrip(current)
      expect(10000) { result.asInstanceOf[DeepObject].depth}
    }
    
    scenario("wide graph") {
      def makeTree(ends: List[WideObject]) : List[WideObject] =
       if(ends.length ==1)
    	   ends
       else {
    	 require(ends.length %8 ==0,ends.length +" length found, must be power of 8")
    	 
         val level : List[WideObject] = ends.grouped(8).map { g => new WideObject(g(0),g(1),g(2),g(3),g(4),g(5),g(6),g(7))} toList
         val tree= makeTree(level)
         tree
       }
      
      val ends = 1 to 4096 map { _=> new WideObject() } toList

      val top = makeTree(ends)//new WideObject(lvl3(0),lvl3(1),lvl3(2),lvl3(3),lvl3(4),lvl3(5),lvl3(6),lvl3(7))
      
      roundTrip(top.head)
    }
  
    scenario("Repeated Object reference") {
      val repeatedObject = new IntObject(5)
      val obj = new MultiObject(repeatedObject,repeatedObject)
      
      val result = roundTrip(obj).asInstanceOf[MultiObject]
      
      result.a should be theSameInstanceAs (result.b)
    }
  
    //graph with objects having bad hashcode/equals that say they are equal when they aren't
  
    //no accessible no arg constructor
  
    //Java lists, sets, maps
  
    //Scala lists, sets, maps
  
    //scala enumerations
  
    //java enumerations
  
    //run parallel using the same Freezer
    
    //comply with transient
  }
  
  def roundTrip(testObject : AnyRef) : AnyRef = {
      val stored = freezer.freeze(testObject)
      val result = freezer.unfreeze(stored)
      testObject should equal(result)
      result
  }
}

case class IntObject(val i:Int) {
  def this() = this(0)
}

case class LongObject(val i:Long) {
  def this() = this(0)
}

case class ByteObject(val i:Byte) {
  def this() = this(0)
}

case class ShortObject(val i:Short) {
  def this() = this(0)
}

case class FloatObject(val i:Float) {
  def this() = this(0.0f)
}

case class DoubleObject(val i:Double) {
  def this() = this(0.0d)
}

case class CharObject(val i:Char) {
  def this() = this('a')
}

case class MultiplePrimitivesObject(b:Byte,s:Short,i:Int,l:Long,f:Float,d:Double,c:Char) {
  def this() = this(0,0,0,0,0.0f,0.0d,'a')
}

case class ObjectObject(val o:IntObject) {
  def this() = this(null)
}

class DeepObject(val o : DeepObject) {
  def this() = this(null)
  
  def depth :Int = depth(1)
  
  @tailrec
  final def depth(count : Int) :Int =  {
    if( o == null)
      count 
    else 
      o.depth(count+1) 
  }
  
  override def equals(that:Any) : Boolean = {
    equalsTail(that)
  }
  
  @tailrec
  private final def equalsTail(that:Any) : Boolean = {
    if(that.isInstanceOf[DeepObject]) {
      val other = that.asInstanceOf[DeepObject]
      
      if(o==null )
        other.o==null
      else
        o.equalsTail(other.o)
    } else {
      false
    }
  }
}

case class MultiObject(val a : AnyRef,val b :AnyRef) {
  def this() = this(null,null)
}

case class WideObject(a : WideObject,b : WideObject,c:WideObject,d:WideObject,e:WideObject,f:WideObject,g:WideObject,h:WideObject) {
  def this() = this(null,null,null,null,null,null,null,null)
}