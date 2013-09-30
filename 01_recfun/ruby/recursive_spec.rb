require_relative 'recursive.rb'

describe "count_change" do
  it "should count_sum" do
    count_sum([50,20,10,5]).should == 85
    count_sum([]).should == 0
  end

  it "should find next_smaller_coin from a list of coins available" do
    coins = [50, 20, 10, 5]

    next_smaller_coin(20, coins.clone).should == 10
    next_smaller_coin(nil, coins.clone).should == 50
    next_smaller_coin(5, coins.clone).should == nil

    next_smaller_coin_loop(20, coins.clone).should == 10
    next_smaller_coin_loop(nil, coins.clone).should == 50
    next_smaller_coin_loop(5, coins.clone).should == nil
  end

  it "should perform retreat step on the sum of coins to continue the search for matches" do
    coins = [30, 20, 10, 5]

    retreat([30, 20, 10], coins).should             == {coins_used_in_sum: [30, 20], next_smaller_coin: 5}
    retreat([30, 20, 5], coins).should              == {coins_used_in_sum: [30],     next_smaller_coin: 10}
    retreat([30, 20, 5, 5], coins).should           == {coins_used_in_sum: [30],     next_smaller_coin: 10}
    retreat([30, 10, 5, 5, 5, 5, 5], coins).should  == {coins_used_in_sum: [30],     next_smaller_coin: 5}
    retreat([30, 20, 10, 5, 5, 5, 5], coins).should == {coins_used_in_sum: [30, 20], next_smaller_coin: 5}
    retreat([5, 5], coins).should == nil

    retreat_loop([30, 20, 10], coins).should             == {coins_used_in_sum: [30, 20], next_smaller_coin: 5}
    retreat_loop([30, 20, 5], coins).should              == {coins_used_in_sum: [30],     next_smaller_coin: 10}
    retreat_loop([30, 20, 5, 5], coins).should           == {coins_used_in_sum: [30],     next_smaller_coin: 10}
    retreat_loop([30, 10, 5, 5, 5, 5, 5], coins).should  == {coins_used_in_sum: [30],     next_smaller_coin: 5}
    retreat_loop([30, 20, 10, 5, 5, 5, 5], coins).should == {coins_used_in_sum: [30, 20], next_smaller_coin: 5}
    retreat_loop([5, 5], coins).should == nil
  end

  it "should count the number of different variants to pay out a sum with given coins" do
    count_change(50, [30, 20, 10, 5]).should == 16

    count_change(4, [1,2]).should == 3
    # count_change(300, [5,10,20,50,100,200,500]).should == 1022
    # count_change(301, [5,10,20,50,100,200,500]).should == 0
    # count_change(300, [500,5,50,100,20,200,10]).should == 1022
    count_change(40, [5,10,20,50]).should == 9

    count_change_loop(50, [30, 20, 10, 5]).should == 16
    count_change_loop(4, [1,2]).should == 3
    count_change_loop(300, [5,10,20,50,100,200,500]).should == 1022
    count_change_loop(301, [5,10,20,50,100,200,500]).should == 0
    count_change_loop(300, [500,5,50,100,20,200,10]).should == 1022
    count_change_loop(40, [5,10,20,50]).should == 9
  end
end