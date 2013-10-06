package funsets

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {
  import FunSets._
  
  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
    val s4 = singletonSet(4)
  }
  
  new TestSets {
    test("singletonSet") {
      assert(contains(s1, 100) == false)
      assert(contains(s2, 2) == true)
    }
    
    test("unionSet") {
      val unionSet = union(s1, s2)
      assert(contains(unionSet, 100) == false)
      assert(contains(unionSet, 1) == true)
      assert(contains(unionSet, 2) == true)
    }
    
    test("intersectSet") {
      val firstUnion  = union(s1, s2)
      val secondUnion = union(s2, s3)
      val intersectSet= intersect(firstUnion, secondUnion)
    
      assert(contains(intersectSet, 1) == false)
      assert(contains(intersectSet, 3) == false)
      assert(contains(intersectSet, 2) == true)
    }
    
    test("diffSet") {
      val firstSet  = union(union(s1, s2), union(s3, s4))
      val secondSet = union(s2, s3)
      val diffSet= diff(firstSet, secondSet)
    
      assert(contains(diffSet, 1) === true)
      assert(contains(diffSet, 2) === false)
      assert(contains(diffSet, 3) === false)
      assert(contains(diffSet, 4) === true)
    }
    
    test("filter") {
      val firstSet  = union(union(s1, s2), union(s3, s4))
      def filterPredicate(x: Int)  = { x >= 3 }
      def filterPredicate2(x: Int) = { x <= 2 }
      val filteredSet  = filter(firstSet, filterPredicate)
      val filteredSet2 = filter(firstSet, filterPredicate2)
      
      assert(contains(filteredSet, 1) === false)
      assert(contains(filteredSet, 2) === false)
      assert(contains(filteredSet, 3) === true)
      assert(contains(filteredSet, 4) === true)
      
      assert(contains(filteredSet2, 1) === true)
      assert(contains(filteredSet2, 2) === true)
      assert(contains(filteredSet2, 3) === false)
      assert(contains(filteredSet2, 4) === false)
    }
    
    test("predicate holds for all elements of set") {
      val firstSet  = union(union(s1, s2), union(s3, s4))
      def holdsForAllElementsPredicate(x: Int)  = { x > -1 }
      def doesNotHoldForAllElementsPredicate(x: Int)  = { x > 2 }
      
      assert(forall(firstSet, holdsForAllElementsPredicate)       === true)
      assert(forall(firstSet, doesNotHoldForAllElementsPredicate) === false)
    }
    
    test("predicate holds for at least one element of a set") {
      val firstSet  = union(union(s1, s2), union(s3, s4))
      def truePredicate(x: Int)  = { x >= 4 }
      def falsePredicate(x: Int) = { x >  4 }
      
      assert(exists(firstSet, truePredicate)  === true)
      assert(exists(firstSet, falsePredicate) === false)
    }
    
    test("map") {
      val firstSet  = union(union(s1, s2), union(s3, s4))
      def mapFunction(x: Int)  = { x * 2 }
      val mappedSet  = map2(firstSet, mapFunction)
      
      assert(contains(mappedSet, 1) === false)
      assert(contains(mappedSet, 2) === true)
      assert(contains(mappedSet, 3) === false)
      assert(contains(mappedSet, 4) === true)
      assert(contains(mappedSet, 6) === true)
      assert(contains(mappedSet, 8) === true)
    }
  }
}
