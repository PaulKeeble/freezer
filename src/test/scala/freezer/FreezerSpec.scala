package freezer

import org.scalatest.FeatureSpec
import org.junit.runner.RunWith
import org.scalatest.GivenWhenThen
import org.scalatest.junit.JUnitRunner
import scala.util.control.TailCalls.TailRec

@RunWith(classOf[JUnitRunner])
class FreezerSpec extends FeatureSpec with GivenWhenThen {
  val freezer = new Freezer
  feature("Freezing null") {
    
    scenario("null") {
      given("a null variable")
      val x = null
      
      when("frozen and unfrozen")
      val stored : Array[Byte] = freezer.freeze(x)
      val result = freezer.unfreeze(stored)
      
      then("the original result is return")
      expect(x) { result }
    }
  }
  
  feature("Freezing primitive fields") {
    scenario("byte") {
      given("an object with an int field and value 1")
      val obj = new ByteObject(1)
      
      when("frozen and unfrozen")
      val stored : Array[Byte] = freezer.freeze(obj)
      val result = freezer.unfreeze(stored)
      
      then("the original field is 1 and the object is of the correct type")
      expect(1) { result.asInstanceOf[ByteObject].i }
    }
    
    scenario("int") {
      given("an object with an int field and value 1")
      val obj = new IntObject(1)
      
      when("frozen and unfrozen")
      val stored : Array[Byte] = freezer.freeze(obj)
      val result = freezer.unfreeze(stored)
      
      then("the original field is 1 and the object is of the correct type")
      expect(1) { result.asInstanceOf[IntObject].i }
    }
    
    scenario("long") {
      given("an object with a long field and value 1")
      val obj = new LongObject(1)
      
      when("frozen and unfrozen")
      val stored : Array[Byte] = freezer.freeze(obj)
      val result = freezer.unfreeze(stored)
      
      then("the original field is 1 and the object is of the correct type")
      expect(1L) { result.asInstanceOf[LongObject].i }
    }
    
    //short
    
    //float
    
    //double
    
    //char
    
    //multiple primitives
  }
  
  feature("Freezing object graphs") {
    scenario("nested object") {
      given("an object with an object field")
      val obj = new ObjectObject(new IntObject(1))
      
      when("frozen and unfrozen")
      val stored : Array[Byte] = freezer.freeze(obj)
      val result = freezer.unfreeze(stored)
      
      then("the sub object is recreated correctly")
      expect(1) { result.asInstanceOf[ObjectObject].o.i }
    }
    
    scenario("deep graph") {
      given("A deep object graph")
      val end = new DeepObject
      var current = end
      
      1 to 10000 foreach { _=>
        current = new DeepObject(current)
      }
      
      when("frozen and unfrozen")
      val stored : Array[Byte] = freezer.freeze(current)
      val result = freezer.unfreeze(stored)
      
      then("the graph is recreated correctly")
      expect(10000) { result.asInstanceOf[DeepObject].depth}
    }
  }
  //simple structured object
  
  //really deep graph  
  
  //wide shallow graph
  
  //same object referenced from two places is a different but shared object
  
  //graph with objects having bad hashcode/equals that say they are equal when they aren't
  
  //arrays
 
  //private no arg constructor
  
  //Java lists, sets, maps
  
  //Scala lists, sets, maps
  
  //scala enumerations
  
  //java enumerations
  
  //run parallel using the same Freezer
  
}

class IntObject(val i:Int) {
  def this() = this(0)
}

class LongObject(val i:Long) {
  def this() = this(0)
}

class ByteObject(val i:Byte) {
  def this() = this(0)
}

class ObjectObject(val o:IntObject) {
  def this() = this(null)
}

class DeepObject(o : DeepObject) {
  def this() = this(null)
  
  def depth :Int = depth(0)
  
  def depth(count : Int) :Int =  {
    if( o == null)
      count 
    else 
      depth(count+1) 
  }
}