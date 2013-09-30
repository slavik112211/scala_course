package recfun

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import scala.collection.mutable.ListBuffer

@RunWith(classOf[JUnitRunner])
class CountChangeSuite extends FunSuite {
  import Main.countChange
  import Main.countSum
  import Main.nextSmallerCoin
  import Main.retreat
  import Main.countChangeDepthSearch
  
  test("countChange") {
    assert(countChange(4,List(1,2)) === 3) //example given in instructions
    assert(countChange(300,List(5,10,20,50,100,200,500)) === 1022) //sorted
    assert(countChange(301,List(5,10,20,50,100,200,500)) === 0) //no pennies
    assert(countChange(300,List(500,5,50,100,20,200,10)) === 1022) //unsorted
    assert(countChange(40,List(5,10,20,50)) === 9) //simple
  }
  
  test("countChangeDepthSearch") {
    assert(countChangeDepthSearch(4,List(1,2)) === 3) //example given in instructions
    assert(countChangeDepthSearch(300,List(5,10,20,50,100,200,500)) === 1022) //sorted
    assert(countChangeDepthSearch(301,List(5,10,20,50,100,200,500)) === 0) //no pennies
    assert(countChangeDepthSearch(300,List(500,5,50,100,20,200,10)) === 1022) //unsorted
    assert(countChangeDepthSearch(40,List(5,10,20,50)) === 9) //simple
  }
  
  test("countSum") {
    assert(countSum(0,List(5,10,20,50).toIterator) === 85)
  }
  
  test("nextSmallerCoin") {
    val coins = List(50,20,10,5).toIterator
    assert(nextSmallerCoin(Some(20),List(50,20,10,5).toIterator) === Some(10))
    assert(nextSmallerCoin(Some(5),List(50,20,10,5).toIterator) === None)
    assert(nextSmallerCoin(None,coins) === Some(50))
  }
  
  test("retreat") {
    val coins = List(50,20,10,5)
    assert(retreat(ListBuffer(30, 20, 10), coins) === Some(ListBuffer(30,20), 5))
    assert(retreat(ListBuffer(30, 20,  5), coins) === Some(ListBuffer(30), 10))
    assert(retreat(ListBuffer(30, 20, 5, 5), coins) === Some(ListBuffer(30), 10))
    assert(retreat(ListBuffer(30, 10, 5, 5, 5, 5, 5), coins) === Some(ListBuffer(30), 5))
    assert(retreat(ListBuffer(30, 20, 10, 5, 5, 5, 5), coins) === Some(ListBuffer(30,20), 5))
    assert(retreat(ListBuffer(5, 5), coins) === None)
    assert(retreat(ListBuffer(), coins) === None)
  }
  
}
