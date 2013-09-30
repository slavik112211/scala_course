require 'debugger'

def pascal(char, row)
  return 1 if char == 0 and row == 0
  upper_row = row-1
  upper_left_char  = char-1
  upper_right_char = char

  if(upper_left_char < 0)
    upper_left_value = 0
  elsif(upper_left_char == 0)
    upper_left_value = 1
  else
    upper_left_value = pascal(upper_left_char, upper_row)
  end

  if(upper_right_char > upper_row)
    upper_right_value = 0
  elsif(upper_right_char == upper_row)
    upper_right_value = 1
  else
    upper_right_value = pascal(upper_right_char, upper_row)
  end

  upper_left_value + upper_right_value
end

#puts pascal(0,4)

def balance(chars)
  balance = true
  opened_brackets = 0
  chars.each_char {|char| 
    if(char == "(")
      opened_brackets += 1
    elsif(char == ")")
      opened_brackets -= 1
      balance = false if opened_brackets<0
    end
  }
  balance = false if opened_brackets != 0
  balance
end

# puts balance("(if (zero? x) max (/ 1 x))")
# puts balance("I told him (that it’s not (yet) done). (But he wasn’t listening)")
# puts balance(":-)")
# puts balance("())(")

def count_sum(coins, sum = 0)
  if(coins.empty?) then sum
  else
    value = coins.shift
    count_sum(coins, sum + value)
  end
end

def next_smaller_coin(coin, coins)
  if(coin.nil?) then coins.first
  elsif(coins.length == 0) then nil
  elsif(coin <= coins.first)
    coins.shift
    next_smaller_coin(coin, coins)
  else coins.first
  end
end

# assume coins array is ordered largest to smallest
def next_smaller_coin_loop(from_coin, coins)
  coin = nil
  return coins.first if from_coin.nil? 
  return nil if coins.nil?

  coins.each do |coin_from_collection|
    if(from_coin > coin_from_collection)
      coin = coin_from_collection
      break
    end
  end
  coin
end

def retreat(coins_used_in_sum, coins)
  if coins_used_in_sum.empty? then nil
  else
    last_coin = coins_used_in_sum.pop
    next_smaller_coin = next_smaller_coin(last_coin, coins.clone)
    if(next_smaller_coin) then {coins_used_in_sum: coins_used_in_sum, next_smaller_coin: next_smaller_coin}
    else retreat(coins_used_in_sum, coins)
    end
  end 
end

def retreat_loop(coins_used_in_sum, coins)
  result = nil
  while !coins_used_in_sum.empty?
    last_coin = coins_used_in_sum.pop
    next_smaller_coin = next_smaller_coin(last_coin, coins.clone)
    if(next_smaller_coin)
      result = {coins_used_in_sum: coins_used_in_sum, next_smaller_coin: next_smaller_coin}
      break
    end
  end
  result
end

def count_change_loop(money, coins)
  coins.sort! { |x,y| y <=> x }
  next_number_to_try ||= coins.first
  coins_used_in_sum = []
  matched_sums_count = 0

  while(true)
    coins_used_in_sum.push next_number_to_try
    current_sum = coins_used_in_sum.inject(sum=0){|sum,x| sum + x }
    
    if(current_sum < money) # keep adding the same coin
    elsif(current_sum >= money)
      # 1. add to matching sums!
      # 2. retreat step:
      #   try substitute last coin with smaller
      #   if smaller exists - fine; else remove the last coin, and try to decrease the next one
      matched_sums_count += 1 if current_sum == money
      retreat_result = retreat(coins_used_in_sum, coins)
      break if retreat_result.nil?
      coins_used_in_sum = retreat_result[:coins_used_in_sum]
      next_number_to_try = retreat_result[:next_smaller_coin]
    end
  end
  matched_sums_count
end

def count_iteration(money, coins, coins_used_in_sum, matched_sums_count, next_number_to_try)
  next_number_to_try ||= coins.first
  coins_used_in_sum.push next_number_to_try
  current_sum = count_sum(coins_used_in_sum.clone)
  
  if(current_sum < money) # keep adding the same coin
    count_iteration(money, coins, coins_used_in_sum, matched_sums_count, next_number_to_try)
  elsif(current_sum >= money)
    # 1. add to matching sums!
    # 2. retreat step:
    #   try substitute last coin with smaller
    #   if smaller exists - fine; else remove the last coin, and try to decrease the next one
    matched_sums_count += 1 if current_sum == money
    retreat_result = retreat(coins_used_in_sum, coins)
    return matched_sums_count if retreat_result.nil?
    count_iteration(money, coins, retreat_result[:coins_used_in_sum], matched_sums_count, retreat_result[:next_smaller_coin])
  end
end

def count_change(money, coins)
  coins.sort! { |x,y| y <=> x }
  count_iteration(money, coins, [], 0, nil)
end



