#!/usr/bin/python

import sys
import os
import xml.etree.ElementTree
import urllib
import urllib2
import json
import socket
import time

messages = {
    'gps103' : 'imei:123456789012345,help me,1201011201,,F,120100.000,A,6000.0000,N,13000.0000,E,0.00,;',
    'tk103' : '(123456789012BP05123456789012345120101A6000.0000N13000.0000E000.0120200000.0000000000L000946BB)',
    'gl100' : '+RESP:GTSOS,123456789012345,0,0,0,1,0.0,0,0.0,1,130.000000,60.000000,20120101120300,0460,0000,18d8,6141,00,11F0,0102120204\0',
    'gl200' : '+RESP:GTFRI,020102,123456789012345,,0,0,1,1,0.0,0,0.0,130.000000,60.000000,20120101120400,0460,0000,18d8,6141,00,,20120101120400,11F0$',
    't55' : '$PGID,123456789012345*0F\r\n$GPRMC,120500.000,A,6000.0000,N,13000.0000,E,0.00,0.00,010112,,*33\r\n',
    'xexun' : '111111120009,+436763737552,GPRMC,120600.000,A,6000.0000,N,13000.0000,E,0.00,0.00,010112,,,A*68,F,, imei:123456789012345,04,481.2,F:4.15V,0,139,2689,232,03,2725,0576\n',
    'totem' : '$$B3123456789012345|AA$GPRMC,120700.000,A,6000.0000,N,13000.0000,E,0.00,,010112,,,A*74|01.8|01.0|01.5|000000000000|20120403234603|14251914|00000000|0012D888|0000|0.0000|3674|940B\r\n',
    'meiligao' : '$$\x00\x60\x12\x34\x56\xFF\xFF\xFF\xFF\x99\x55120900.000,A,6000.0000,N,13000.0000,E,0.00,,010112,,*1C|11.5|194|0000|0000,0000\x69\x62\x0D\x0A',
    'suntech' : 'SA200STT;123456;042;20120101;12:11:00;16d41;-15.618767;-056.083214;000.011;000.00;11;1;41557;12.21;000000;1;3205\r',
    'h02' : '*HQ,123456789012345,V1,121300,A,6000.0000,N,13000.0000,E,0.00,0.00,010112,ffffffff,000000,000000,000000,000000#',
    'jt600' : '$\x00\x00\x12\x34\x56\x11\x00\x1B\x01\x01\x12\x12\x14\x00\x60\x00\x00\x00\x13\x00\x00\x00\x0F\x00\x00\x07\x50\x00\x00\x00\x2B\x91\x04\x4D\x1F\xA1',
    'v680' : '#123456789012345#1000#0#1000#AUT#1#66830FFB#13000.0000,E,6000.0000,N,001.41,259#010112#121600##',
    'pt502' : '$POS,123456,121700.000,A,6000.0000,N,13000.0000,E,0.0,0.0,010112,,,A/00000,00000/0/23895000//\r\n',
    'tr20' : '%%123456789012345,A,120101121800,N6000.0000E13000.0000,0,000,0,01034802,150,[Message]\r\n',
    'meitrack' : '$$d138,123456789012345,AAA,35,60.000000,130.000000,120101122000,A,7,18,0,0,0,49,3800,24965,510|10|0081|4F4F,0000,000D|0010|0012|0963|0000,,*BF\r\n',
    'megastek' : 'STX,102110830074542,$GPRMC,122400.000,A,6000.0000,N,13000.0000,E,0.00,0.00,010112,,,A*64,F,LowBattery,imei:123456789012345,03,113.1,Battery=24%,,1,460,01,2531,647E;57\r\n',
    'gpsgate' : '$FRLIN,IMEI,123456789012345,*7B\r\n$GPRMC,122600.000,A,6000.00000,N,13000.00000,E,0.000,0.0,010112,,*0A\r\n',
    'tlt2h' : '#123456789012345#V500#0000#AUTO#1\r\n#$GPRMC,123000.000,A,6000.0000,N,13000.0000,E,0.00,0.00,010112,,,D*70\r\n##\r\n',
    'taip' : '>REV481669045060+6000000-1300000000000012;ID=123456789012345<',
    'wondex' : '123456789012345,20120101123200,130.000000,60.000000,0,000,0,0,2\r\n',
    'ywt' : '%RP,123456789012345:0,120101123500,E130.000000,N60.000000,,0,0,4,0,00\r\n',
    'tk102' : '[!0000000081r(123456789012345,TK102-W998_01_V1.1.001_130219,255,001,255,001,0,100,100,0,internet,0000,0000,0,0,255,0,4,1,11,00)][=00000000836(ITV123600A6000.0000N13000.0000E000.00001011210010000)]',
    'wialon' : '#L#123456789012345;test\r\n#SD#010112;123900;6000.0000;N;13000.0000;E;0;0;0;4\r\n',
    'carscop' : '*040331141830UB05123456789012345010112A6000.0000N13000.0000E000.0124000000.0000000000L000000^',
    'manpower' : 'simei:123456789012345,,,tracker,51,24,1.73,120101124200,A,6000.0000,N,13000.0000,E,0.00,28B9,1DED,425,01,1x0x0*0x1*60x+2,en-us,;',
    'globalsat' : '$123456789012345,1,1,010112,124300,E13000.0000,N6000.0000,00000,0.0100,147,07,2.4!',
    'pt3000' : '%123456789012345,$GPRMC,124500.000,A,6000.0000,N,13000.0000,E,0.00,,010112,,,A,+100000000000,N098d',
    'topflytech' : '(123456789012345BP00XG00b600000000L00074b54S00000000R0C0F0014000100f0120101124700A6000.0000N13000.0000E000.0000.00)',
    'laipac' : '$AVRMC,123456789012345,124800,a,6000.0000,N,13000.0000,E,0.00,0.00,010112,0,3.727,17,1,0,0*17\r\n',
    'gotop' : '#123456789012345,CMD-T,A,DATE:120101,TIME:125000,LAT:60.0000000N,LOT:130.0000000E,Speed:000.0,84-20,000#',
    'sanav' : 'imei:123456789012345rmc:$GPRMC,093604.354,A,4735.0862,N,01905.2146,E,0.00,0.00,171013,,*09,AUTO-4103mv',
    'easytrack' : '*ET,123456789012345,DW,A,0A090D,101C0D,00CF27C6,0413FA4E,0000,0000,00000000,20,4,0000,00F123#',
    'gpsmarker' : '$GM200123456789012345T100511123300N55516789E03756123400000035230298#\r',
    'stl060' : '$1,123456789012345,D001,AP29AW0963,23/02/14,14:06:54,17248488N,078342226E,0.08,193.12,1,1,1,1,1,A#',
    'cartrack' : '$$123456????????&A9955&B102904.000,A,2233.0655,N,11404.9440,E,0.00,,030109,,*17|6.3|&C0100000100&D000024?>&E10000000##',
    'minifinder' : '!1,123456789012345;!A,01/01/12,12:15:00,60.000000,130.000000,0.0,25101,0;',
    'haicom' : '$GPRS123456789012345,T100001,150618,230031,5402267400332464,0004,2014,000001,,,1,00#V040*',
    'box' : 'H,BT,123456789012345,081028142432,F5813D19,6D6E6DC2\rL,081028142429,G,52.51084,-1.70849,0,170,0,1,0\r',
    'freedom' : 'IMEI,123456789012345,2014/05/22, 20:49:32, N, Lat:4725.9624, E, Lon:01912.5483, Spd:5.05\r\n',
    'telic' : '182012345699,010100001301,0,270613041652,166653,475341,3,0,355,6,2,1,231,8112432,23201,01,00,217,0,0,0,0,7\0',
    'trackbox' : 'a=connect&v=11&i=123456789012345\r\n183457.999,5126.0247N,00002.8686E,5.2,70.4,3,57.63,32.11,17.32,150507,05\r\n',
    'visiontek' : '$1,AP09BU9397,123456789012345,20,06,14,15,03,28,17267339N,078279407E,060.0,073,0550,11,0,1,0,0,1,1,26,A,0000000000#',
    'tr900' : '>123456,4,1,150626,131252,W05830.2978,S3137.2783,,00,348,18,00,003-000,0,3,11111011*3b!\r\n',
    'ardi01' : '123456789012345,20141010052719,24.4736042,56.8445807,110,289,40,7,5,78,-1\r\n',
    'xt013' : 'TK,123456789012345,150131090859,+53.267863,+5.767363,0,38,12,0,F,204,08,C94,336C,24,,4.09,1,,,,,,,,\r\n',
    'gosafe' : '*GS16,123456789012345,100356130215,,SYS:G79W;V1.06;V1.0.2,GPS:A;6;N24.802700;E46.616828;0;0;684;1.35,COT:60,ADC:4.31;0.10,DTT:20000;;0;0;0;1#',
    'xirgo' : '$$123456789012345,6001,2013/01/22,15:36:18,25.80907,-80.32531,7.1,19,165.2,11,0.8,11.1,17,1,1,3.9,2##',
    'mtx' : '#MTX,123456789012345,20101226,195550,41.6296399,002.3611174,000,035,000000.00,X,X,1111,000,0,0\r\n',
    'aquila' : '$$SRINI_1MS,123456,1,12.963515,77.533844,150925161628,A,27,0,8,0,68,0,0,0,0,0,0,0,0,1,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,*43\r\n',
    'flextrack' : '-1,LOGON,123456,8945000000\r-2,UNITSTAT,20060101,123442,1080424008,N0.00.0000,E0.00.0000,0,0,0,4129,-61,2,23866,0,999,A214,63,2EE2,3471676\r',
    'watch' : '[SG*123456*0087*UD,220414,134652,A,22.571707,N,113.8613968,E,0.1,0.0,100,7,60,90,1000,50,0000,4,1,460,0,9360,4082,131,9360,4092,148,9360,4091,143,9360,4153,141]'
}

baseUrl = 'http://localhost:8082'
user = { 'email' : 'admin', 'password' : 'admin' }

debug = '-v' in sys.argv

def load_ports():
    ports = {}
    dir = os.path.dirname(os.path.abspath(__file__))
    root = xml.etree.ElementTree.parse(dir + '/../setup/default.xml').getroot()
    for entry in root.findall('entry'):
        key = entry.attrib['key']
        if key.endswith('.port'):
            ports[key[:-5]] = int(entry.text)
    if debug:
        print '\nports: %s\n' % repr(ports)
    return ports

def login():
    request = urllib2.Request(baseUrl + '/api/session')
    response = urllib2.urlopen(request, urllib.urlencode(user))
    if debug:
        print '\nlogin: %s\n' % repr(json.load(response))
    return response.headers.get('Set-Cookie')

def remove_devices(cookie):
    request = urllib2.Request(baseUrl + '/api/devices')
    request.add_header('Cookie', cookie)
    response = urllib2.urlopen(request)
    data = json.load(response)
    if debug:
        print '\ndevices: %s\n' % repr(data)
    for device in data:
        request = urllib2.Request(baseUrl + '/api/devices/' + str(device['id']))
        request.add_header('Cookie', cookie)
        request.get_method = lambda: 'DELETE'
        response = urllib2.urlopen(request)

def add_device(cookie, unique_id):
    request = urllib2.Request(baseUrl + '/api/devices')
    request.add_header('Cookie', cookie)
    request.add_header('Content-Type', 'application/json')
    device = { 'name' : unique_id, 'uniqueId' : unique_id }
    response = urllib2.urlopen(request, json.dumps(device))
    data = json.load(response)
    return data['id']

def send_message(port, message):
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect(('127.0.0.1', port))
    s.send(message)
    s.close()

def get_protocols(cookie, device_id):
    params = { 'deviceId' : device_id, 'from' : '2000-01-01T00:00:00.000Z', 'to' : '2050-01-01T00:00:00.000Z' }
    request = urllib2.Request(baseUrl + '/api/positions?' + urllib.urlencode(params))
    request.add_header('Cookie', cookie)
    request.add_header('Content-Type', 'application/json')
    request.add_header('Accept', 'application/json')
    response = urllib2.urlopen(request)
    protocols = []
    for position in json.load(response):
        protocols.append(position['protocol'])
    return protocols

ports = load_ports()

cookie = login()
remove_devices(cookie)

devices = {
    '123456789012345' : add_device(cookie, '123456789012345'),
    '123456789012' : add_device(cookie, '123456789012'),
    '123456' : add_device(cookie, '123456')
}


all = set(ports.keys())
protocols = set(messages.keys())

print 'Total: %d' % len(all)
print 'Missing: %d' % len(all - protocols)
print 'Covered: %d' % len(protocols)

#if all - protocols:
#    print '\nMissing: %s\n' % repr(list((all - protocols)))

for protocol in messages:
    send_message(ports[protocol], messages[protocol])

time.sleep(10)

for device in devices:
    protocols -= set(get_protocols(cookie, devices[device]))

print 'Success: %d' % (len(messages) - len(protocols))
print 'Failed: %d' % len(protocols)

if protocols:
    print '\nFailed: %s' % repr(list(protocols))
