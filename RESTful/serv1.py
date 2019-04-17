from flask import Flask
from flask_restful import reqparse, abort, Api, Resource
import psutil
#sudo pip install psutil

app = Flask(__name__)
api = Api(app)

parser = reqparse.RequestParser()
parser.add_argument('text')

class TodoList(Resource):

    def post(self):
        args = parser.parse_args()
        return {'size': len(args['text'].split())}, 201

class cpu(Resource):

    def get(self):
        cpu_percent = psutil.cpu_percent()
        print cpu_percent
        return {'cpu': cpu_percent}, 201

api.add_resource(TodoList, '/')
api.add_resource(cpu, '/cpu')

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=7000, debug=True)