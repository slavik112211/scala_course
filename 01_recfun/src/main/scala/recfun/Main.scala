package recfun
import common._
import scala.collection.mutable.ListBuffer

object Main {
  /**
   * Exercise 1: Pascal’s Triangle
   * The following pattern of numbers is called Pascal’s triangle.
   *     1
   *    1 1
   *   1 2 1
   *  1 3 3 1
   * 1 4 6 4 1
   *    ...
   * The numbers at the edge of the triangle are all 1, and each number 
   * inside the triangle is the sum of the two numbers above it. Write a 
   * function that computes the elements of Pascal’s triangle by means 
   * of a recursive process.
   * Do this exercise by implementing the pascal function in Main.scala, 
   * which takes a column c and a row r, counting from 0 and returns the number 
   * at that spot in the triangle. For example, pascal(0,2)=1, 
   * pascal(1,2)=2 and pascal(1,3)=3.
   */
  def pascal(c: Int, r: Int): Int = {
    if(c == 0 && r == 0) return 1
    val upper_row = r-1
    val upper_left_char  = c-1
    val upper_right_char = c
    
    def upper_left_value = {
      if(upper_left_char < 0) 0
	  else if(upper_left_char == 0) 1
	  else pascal(upper_left_char, upper_row)
    }
    
    def upper_right_value = {
      if(upper_right_char > upper_row) 0
	  else if(upper_right_char == upper_row) 1
	  else pascal(upper_right_char, upper_row)
    }
    
    upper_left_value + upper_right_value
  }
  
  /**
   * Exercise 2: Parentheses Balancing
   * Write a recursive function which verifies the balancing of parentheses in a string
   */
  def balance(chars: List[Char]): Boolean = {
    
	  def balanceIter(chars: List[Char], openedBrackets: Int): Boolean = {
	    if(openedBrackets < 0) return false
	    if(chars.length == 0){
	      if(openedBrackets == 0) return true
	      else return false
	    } 
	    
	    val currentChar = chars.head
	    if(currentChar.toString == "(")
	      balanceIter(chars.drop(1), openedBrackets+1)
	    else if(currentChar.toString == ")")
	      balanceIter(chars.drop(1), openedBrackets-1)
	    else
	      balanceIter(chars.drop(1), openedBrackets) 
	  }
    
    balanceIter(chars, 0)
  }
  
  /**
   * Write a recursive function that counts how many different 
   * ways you can make change for an amount, given a list of 
   * coin denominations. For example, there are 3 ways to give 
   * change for 4 if you have coins with denomination 1 and 2: 
   * 1+1+1+1, 1+1+2, 2+2.
   * 
   * Algorithm is taken from 'Structure and Interpretation of Computer Programs SICP'
   * 1.2.2 Tree Recursion
   */
  def countChange(money: Int, coins: List[Int]): Int = {
    
    def removeCoinsLargerThanSum(coins: List[Int]): List[Int] = {
      if(coins.last > money) removeCoinsLargerThanSum(coins.dropRight(1))
      else coins
    }

    def countSum(sum: Int, coins: List[Int]) : Int = {
      if(sum == 0) 1
      else if (sum < 0) 0
      else if (coins.length == 0) 0
      else {
	      val highestDenominationCoin = coins.last
	      countSum(sum, coins.dropRight(1)) + countSum(sum - highestDenominationCoin, coins: List[Int])
      }
    }
    val coinsSorted = removeCoinsLargerThanSum(coins.sortWith((e1, e2) => (e1 < e2)))
    countSum(money, coinsSorted)
  }
 
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
  }

  def countSum(sum: Int = 0, coins: Iterator[Int]): Int = {
    if(!coins.hasNext) sum
    else countSum(sum + coins.next, coins)
  }
  
  // assumes coins are ordered largest->smallest
  def nextSmallerCoin(coin: Option[Int], coins: Iterator[Int]): Option[Int] = {
    if(coin.isEmpty && coins.hasNext) Some(coins.next)
    else if(!coins.hasNext) None
    else {
      val currentCoin = coins.next
      if(coin.get <= currentCoin) nextSmallerCoin(coin, coins)
      else Some(currentCoin)
    }
  }
  
  // try substitute last coin with smaller
  // if smaller exists - fine; else remove the last coin, and try to decrease the next one
  def retreat(coinsUsedInSum: ListBuffer[Int], coins: List[Int]): Option[(ListBuffer[Int], Int)] = {
    if(coinsUsedInSum.length == 0) None
    else {
      val nextSmallerCoinValue = nextSmallerCoin(Some(coinsUsedInSum.last), coins.toIterator)
      if(!nextSmallerCoinValue.isEmpty){
        Some((coinsUsedInSum.dropRight(1), nextSmallerCoinValue.get))
      } else retreat(coinsUsedInSum.dropRight(1), coins)
    }
  }
  
  /**
   * Write a recursive function that counts how many different 
   * ways you can make change for an amount, given a list of 
   * coin denominations. For example, there are 3 ways to give 
   * change for 4 if you have coins with denomination 1 and 2: 
   * 1+1+1+1, 1+1+2, 2+2.
   * 
   * Algorithm explores all possible partitions of a set of numbers.
   * http://en.wikipedia.org/wiki/Partition_%28number_theory%29
   * It checks all possible partitions from using largest numbers to smallest. 
   * The progression is as follows:
   * 
   * sum 60 with 30, 20, 10, 5.
   * 30 (<); 30 30 (==)
   * 30 20 (<); 30 20 20 (>)
   * 30 20 10 (==)
   * 30 20 5 (<); 30 20 5 5 (==)
   * 30 10 (<); 30 10 10 (<); 30 10 10 10 (==)
   * 30 10 10 5 (<); 30 10 10 5 5 (==)
   * 30 10 5 (<); 30 10 5 5 (<); 30 10 5 5 5 (<); 30 10 5 5 5 5 (==)
   * 30 5 (<); 30 5 5 (<); 30 5 5 5 (<); 30 5 5 5 5 (<); 30 5 5 5 5 5 (<); 30 5 5 5 5 5 5 (==)
   * 20 (<); 20 20 (<); 20 20 20 (==);
   * 20 20 10 (<); 20 20 10 10 (==);
   * ...
   */
  def countChangeDepthSearch(money: Int, coins: List[Int]): Int = {
    val coinsSorted = coins.sortWith((e1, e2) => (e1 > e2))
    
    def getNextNumberToTry(nextNumberToTry: Option[Int]) : Int = {
      if(nextNumberToTry.isEmpty) coinsSorted.head
      else nextNumberToTry.get
    }
    
    def countChangeIteration(coinsUsedInSum: ListBuffer[Int], matchedSumsCount: Int, nextNumberToTry: Option[Int]) : Int = {
      coinsUsedInSum.append(getNextNumberToTry(nextNumberToTry))
      val currentSum = countSum(0, coinsUsedInSum.toIterator)
      if(currentSum < money) // keep adding the same coin
        countChangeIteration(coinsUsedInSum, matchedSumsCount, Some(getNextNumberToTry(nextNumberToTry)))
      else { // currentSum >= money
        val retreatResult = retreat(coinsUsedInSum, coinsSorted)
        if(retreatResult.isEmpty && currentSum == money) matchedSumsCount+1
        else if(retreatResult.isEmpty) matchedSumsCount
        else if(currentSum == money)
          countChangeIteration(retreatResult.get._1, matchedSumsCount+1, Some(retreatResult.get._2))
        else
          countChangeIteration(retreatResult.get._1, matchedSumsCount, Some(retreatResult.get._2))
      }
    }
    countChangeIteration(ListBuffer(), 0, None)
  }
}
