package freezer

import org.scalatest.FeatureSpec
import org.junit.runner.RunWith
import org.scalatest.GivenWhenThen
import org.scalatest.junit.JUnitRunner

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
    scenario("int") {
      given("an object with an int field and value 1")
      val obj = new IntObject(1)
      
      when("frozen and unfrozen")
      val stored : Array[Byte] = freezer.freeze(obj)
      val s = new String(stored,"UTF-8")
      val result = freezer.unfreeze(stored)
      
      then("the original field is 1 and the object is of the correct type")
      expect(1) { result.asInstanceOf[IntObject].i }
    }
    
    
  }
  
  //null
  
  //simple structured object
  
  //really deep graph
  
  //wide shallow graph
  
  //arrays
 
  //Java lists, sets, maps
  
  //Scala lists, sets, maps
  
  //scala enumerations
  
  //java enumerations
}

class IntObject(val i:Int) {
  def this() = this(0)
}