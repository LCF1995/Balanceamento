from flask import Flask
from flask_restful import reqparse, abort, Api, Resource
import requests
import psutil
import socket
#sudo pip install psutil

app = Flask(__name__)
api = Api(app)

parser = reqparse.RequestParser()
parser.add_argument('text')

serv1 = '192.168.43.134:6000'
serv2 = '192.168.43.134:7000'

class TodoList(Resource):

    def post(self):
        args = parser.parse_args()

        s1 = 0
        try:
            s1 = (requests.get('http://'+serv1+'/cpu', json={})).json()
        except Exception, e:
            pass

        s2 = 0
        try:
            s2 = (requests.get('http://'+serv2+'/cpu', json={})).json()
        except Exception, e:
            pass


        if s1 != 0 and s2 != 0:
            print 'servs ok'

            if float(s1['cpu']) >= float(s2['cpu']):
                return requests.post('http://'+serv1, json={"text": args['text']}).json(), 201
            else:
                return requests.post('http://'+serv2, json={"text": args['text']}).json(), 201

            return args, 201
        else:
            if s1:
                print "Serv 2 off"
                return requests.post('http://'+serv1, json={"text": args['text']}).json(), 201
            elif s2:
                print "Serv 1 off"
                return requests.post('http://'+serv2, json={"text": args['text']}).json(), 201
            else:
                print "servidor 1 e 2 fora do ar"
                return {"size": 0}, 201

api.add_resource(TodoList, '/')

if __name__ == '__main__':
    app.run(host='192.168.43.181', port=3001,debug=True)