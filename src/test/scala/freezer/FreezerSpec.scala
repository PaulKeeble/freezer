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
    
    //short
    
    //float
    
    //double
    
    //char
    
    //multiple primitives
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
      
      1 to 10000 foreach { _=>
        current = new DeepObject(current)
      }
      val result = roundTrip(current)
      expect(10001) { result.asInstanceOf[DeepObject].depth}
    }
    //wide shallow graph
  
    //same object referenced from two places is a different but shared object
  
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