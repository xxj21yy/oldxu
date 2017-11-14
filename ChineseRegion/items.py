# -*- coding: utf-8 -*-

import scrapy


class ChineseregionItem(scrapy.Item):
    code = scrapy.Field()
    name = scrapy.Field()
    pcode = scrapy.Field()
