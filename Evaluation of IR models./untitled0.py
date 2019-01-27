# -*- coding: utf-8 -*-


import json
# if you are using python 3, you should 
import urllib.request
import urllib.parse
#import urllib2


# change the url according to your own corename and query
#inurl='http://localhost:8983/solr/BM25/select?indent=on&q=Anti-Refugee%20Rally%20in%20Dresden&wt=json&fl=id%2Cscore&indent=true&rows=20'
#inurl='http://localhost:8983/solr/VSM/select?indent=on&q=Бильд.%20Внутренний%20документ%20говорит,%20что%20Германия%20примет%201,5%20млн%20беженцев%20в%20этом%20году&wt=json&fl=id%2Cscore&indent=true&rows=20'
#inurl='http://localhost:8983/solr/VSM/select?indent=on&q=Бильд.%20Внутренний%20документ%20говорит,%20что%20Германия%20примет%201,5%20млн%20беженцев%20в%20этом%20году&wt=json&fl=id%2Cscore&indent=true&rows=20'
#inurl = 'http://localhost:8983/solr/VSM/select?q=US air dropped 50 tons of Ammo on Syria&fl=id%2Cscore&wt=json&indent=true'
outfn = '/Users/ektakatiyar/Downloads/IR/project3_data/BM25/output.txt'

inputfile = open('queries.txt',encoding='UTF-8')
count = 0

outf = open(outfn, 'w',encoding='UTF-8')
for i,line in enumerate(inputfile):
    line = line[4:]
    line = line[:-1] 
    print(line)
    #outf = open(outfn+format(i+1)+".txt", 'w',encoding='UTF-8')
    
    #line = "%20".join(line.split(" "))
    line = urllib.parse.quote(line)
    
    #print(line)    
    inurl = "http://localhost:8983/solr/BM25/select?defType=dismax&search_type=dfs_query_then_fetch&qs=20&indent=on&q="+line+"&wt=json&fl=id%2Cscore&indent=true&rows=20"
    #inurl = "http://localhost:8983/solr/BM25/select?defType=dismax&search_type=dfs_query_then_fetch&pf=text_en^1.2+text_de^1.2+text_ru^1.2&qf=text_en^3.0+text_de^3.0+text_ru^3.0&qs=20&q="+line+"&wt=json&fl=id%2Cscore&indent=true&rows=20"
    #print(inurl)
    data = urllib.request.urlopen(inurl)
    #print(data)
    
# change query id and IRModel name accordingly
    qid = '{0:03}'.format(i+1)
    IRModel='BM25'    
#data = urllib2.urlopen(inurl)
# if you're using python 3, you should use
#new_url=urllib.parse.quote(inurl)
    data = urllib.request.urlopen(inurl)
#data = urllib.request.urlopen(new_url).read()
    docs = json.load(data)['response']['docs']
    
# the ranking should start from 1 and increase
    rank = 1
    for doc in docs:
        outf.write(qid + ' ' + 'Q0' + ' ' + format(doc['id']) + ' ' + format(rank) + ' ' + format(doc['score']) + ' ' + IRModel + '\n')
        rank += 1
outf.close()
        
        #outf = open(outfn, 'w',encoding='UTF-8')
