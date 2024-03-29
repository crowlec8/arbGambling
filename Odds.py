from pip._vendor import requests
import json

region = "eu"
mkt = "h2h"
totalProfits = 0.0
totalStakes = 0
getSports = "https://api.the-odds-api.com/v3/sports/?apiKey="+apiKey

def profits(setOfOdds, stakes, profitList, marginList, loop):
    firstOdd = setOfOdds[0]
    secondOdd = setOfOdds[1] 
    currentLowestProfit = -100.0
    for x in range(5, 100, 5):
        for y in range(5, 100, 5):
            firstWinnings = x * (firstOdd - 1)
            secondWinnings = y * (secondOdd - 1)
            profit1 = firstWinnings - y
            profit2 = secondWinnings - x
            profitMargin1 = profit1/(x+y)
            profitMargin2 = profit2/(x+y)
            if(profitMargin1 < profitMargin2 ):
                lowestProfit = profitMargin1
            else:
                lowestProfit = profitMargin2 
            if(lowestProfit > currentLowestProfit):
                currentLowestProfit = lowestProfit
                currentX = x
                currentY = y
                currentProfit1 = profit1
                currentProfit2 = profit2
                currentMargin1 = profitMargin1
                currentMargin2 = profitMargin2
    stakes[loop][0] = currentX
    stakes[loop][1] = currentY
    stakes.append([0, 0])
    profitList[loop][0] = currentProfit1
    profitList[loop][1] = currentProfit2
    profitList.append([0.0, 0.0])
    marginList[loop][0] = currentMargin1
    marginList[loop][1] = currentMargin2
    marginList.append([0.0, 0.0])

def isBetterOdd(siteOdd, siteName, sitesList, bestOdds, loop):
    if(siteOdd[0] > bestOdds[loop][0])&(len(siteOdd) == 2)&(siteName != 'Betfair')&(siteName != 'Matchbook'):
        bestOdds[loop][0] = siteOdd[0]
        sitesList[loop][0] = siteName
    if(siteOdd[1] > bestOdds[loop][1])&(len(siteOdd) == 2)&(siteName != 'Betfair')&(siteName != 'Matchbook'): 
        bestOdds[loop][1] = siteOdd[1]
        sitesList[loop][1] = siteName

def sports_keys():
    #response = requests.get(getSports)
    #sports = response.json()
    sports = json.load(open('APISports.json'))
    data = sports['data']
    sportsKeys = []
    for sportsGroups in data:
        sportsKeys.append(sportsGroups['key'])
    return sportsKeys

def getUrl(sports):
    #getOdds = "https://api.the-odds-api.com/v3/odds/?apiKey={}&sport={}&region={}&mkt={}".format(apiKey, sports, region ,mkt)
    #response = requests.get(getOdds)
    #oddsPage = response.json()
    oddsPage = json.load(open('APIOdds.json'))
    data = oddsPage['data'] 
    teamsList = []
    sitesList = [["", ""]]
    bestOdds = [[0.0, 0.0]]
    stakes = [[0, 0]]
    profitList = [[0.0, 0.0]]
    marginList = [[0.0, 0.0]] 
    loop = 0
    for games in data:
        teamsList.append(games['teams'])
        sites = games['sites']
        for site in sites:
            siteOdd = site['odds']['h2h']
            siteName = site['site_nice']
            isBetterOdd(siteOdd, siteName, sitesList, bestOdds, loop)
        loop = loop + 1
        sitesList.append(["", ""])
        bestOdds.append([0.0, 0.0])
    bestOdds.pop()
    sitesList.pop()
    loop = 0
    for setOfOdds in bestOdds:
        profits(setOfOdds, stakes, profitList, marginList, loop)
        loop = loop + 1
    stakes.pop()
    profitList.pop()
    marginList.pop()
    loop = 0
    for setOfProfits in profitList:
        if(setOfProfits[0]>=0) & (setOfProfits[1]>=0):
            print(sports)
            break
    global totalProfits
    global totalStakes
    for setOfProfits in profitList:
        if(setOfProfits[0]>=0) & (setOfProfits[1]>=0):
            totalStakes = totalStakes + stakes[loop][0] + stakes[loop][1]
            if(setOfProfits[0] < setOfProfits[1]):
                totalProfits = totalProfits + setOfProfits[0]
            else:
                totalProfits = totalProfits + setOfProfits[1]
            print("Bet $" + str(stakes[loop][0]) + " on the " + teamsList[loop][0] + " with " + sitesList[loop][0] + " at " + str(setOfOdds[0]))
            print("And bet $" + str(stakes[loop][1]) + " on the " + teamsList[loop][1] + " with " + sitesList[loop][1] + " at " + str(setOfOdds[1]))
            print("If " + teamsList[loop][0] + " win, your profit is $" + str(round(profitList[loop][0], 2)) + " and if " + teamsList[loop][1] + " win, your profit is $" + str(round(profitList[loop][1], 2)) + "\n")
        loop = loop + 1
    breakpnt = 0
    print(str(marginList) + "\n")
    

sportsKeys = sports_keys()
#for sports in sportsKeys:
getUrl(sportsKeys[3])
print("Spend $" + str(totalStakes) + " and your total profit avaiable right now = $" + str(round(totalProfits, 2)))
ret = (totalProfits/totalStakes) * 100
print("Return on investments is roughly " + str(round(ret, 2)) + " percent right now")
