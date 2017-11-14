# -*- coding: utf-8 -*-
from scrapy.http import Request
from scrapy.linkextractors import LinkExtractor
from scrapy.spiders import CrawlSpider, Rule
from MySql import *

import logging

logging.basicConfig(level=logging.DEBUG,
                    format='%(asctime)s %(filename)s[line:%(lineno)d] %(levelname)s %(message)s',
                    datefmt='%a, %d %b %Y %H:%M:%S',
                    filename='myapp.log',
                    filemode='w')
class RegionSpider(CrawlSpider):
    name = "region"
    allowed_domains = ["stats.gov.cn"]
    start_urls = [
        "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2016/index.html",
    ]

    # 市
    def parse_item(self, response):
        logging.info('***************' + response.url + '****************************')
        thisleng =  len(response.css('.citytr'))
        self.title += thisleng
        logging.info ("now="+str(thisleng)+"*********title="+str(self.title))

        pid = response.meta['id']
        type =1
        pname = response.meta['name']
        for city in response.css('.citytr'):
            purl = city.xpath('td[1]/a/@href').extract()[0]
            myid = purl[purl.rfind('/') + 1:purl.rfind('.')]
            myname = city.xpath('td[2]/a/text()').extract()[0]
            insert1(myid,myname,pid,type,pname)
            url = response.urljoin(purl)
            yield Request(url, meta={"id": myid, 'name': pname + "/" + myname}, callback=self.parse2)

    # 县城
    def parse2(self, response):
        logging.info( '***************' + response.url + '****************************')
        thisleng =  len(response.css('.countytr'))
        self.title += thisleng
        logging.info( "now="+str(thisleng)+"*********title="+str(self.title))


        pid = response.meta['id']
        type = 2
        pname = response.meta['name']
        for county in response.css('.countytr'):
            if len(county.xpath('td[1]/a/text()')) > 0:
                purl = county.xpath('td[1]/a/@href').extract()[0]
                myid = purl[purl.rfind('/') + 1:purl.rfind('.')]
                myname = county.xpath('td[2]/a/text()').extract()[0]
                url = response.urljoin(purl)

                insert1(myid, myname, pid, type, pname)
                yield Request(url, meta={"id": myid, 'name': pname + "/" + myname}, callback=self.oarse3)
            else:
                myid = county.xpath('td[1]/text()').extract()[0]
                myname = county.xpath('td[2]/text()').extract()[0]
                insert1(myid, myname, pid, 88, pname)

    # 城镇
    def oarse3(self, response):
        logging.info( '***************' + response.url + '****************************')
        thisleng =  len(response.css('.towntr'))
        self.title += thisleng
        logging.info( "now="+str(thisleng)+"*********title="+str(self.title))


        pid= response.meta['id']
        type=3
        pname =  response.meta['name']
        for town in response.css('.towntr'):
            purl = town.xpath('td[1]/a/@href').extract()[0]
            myid = purl[purl.rfind('/') + 1:purl.rfind('.')]
            myname = town.xpath('td[2]/a/text()').extract()[0]
            url = response.urljoin(purl)
            insert1(myid, myname, pid, type, pname)
            yield Request(url, meta={"id": myid, 'name':  pname + "/" + myname}, callback=self.parse4)

    def parse4(self, response):
        logging.info( '***************' + response.url + '****************************')
        thisleng =  len(response.css('.villagetr'))
        self.title += thisleng
        logging.info( "now="+str(thisleng)+"*********title="+str(self.title))


        pid = response.meta['id']
        pname =  response.meta['name']
        for village in response.css('.villagetr'):
            village.xpath('td[3]/text()').extract()[0]
            myid = village.xpath('td[1]/text()').extract()[0]
            type = village.xpath('td[2]/text()').extract()[0]
            myname = village.xpath('td[3]/text()').extract()[0]
            insert1(myid, myname, pid, type, pname)

    # 省
    def parse_start_url(self, response):
        type = 0
        links = response.css('.provincetable tr td a')
        self.title=len(links)
        for index in range(len(links)):
            # 这里建议用多进程要不或很慢  一共31个组  其中
            # if index!=1:
            #     continue
            link1 = links[index]
            url = link1.xpath('@href').extract()[0]
            name = link1.xpath('text()').extract()[0]
            urltemp = url[0:url.find('.')]
            insert(urltemp,name,type)
            url = response.urljoin(url)  # 补全
            logging.info(url)
            logging.info('*' * 30)
            yield Request(url, meta={"id": urltemp, 'name': name}, callback=self.parse_item)
