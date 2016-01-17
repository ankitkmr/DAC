from flask import Flask,request
from collections import deque
app = Flask(__name__)

class Cache(object):
    def __init__(self):
        self.cache = {}
        self.count = 0
        self.queue = deque([])
        
    def Calculate(operand1,operand2,operator):
        if operator == '+':
            result = operand1 + operand2
        elif operator == '-':
            result = operand1 - operand2
        elif operator == '*':
            result = operand1 * operand2
        elif operator == '/':
            result = operand1 / operand2
        elif operator == '%':
            result = operand1 % operand2
        elif operator == '^':
            result = operand1 ** operand2
        else:
            result = None
        return str(result)

    def check(self,operand1,operand2,operator):
        if operand1 in self.cache:
            if operand2 in self.cache[operand1]:
                if operator in self.cache[operand1][operand2]:
                    print "Returning from Cache"
                    return self.cache[operand1][operand2][operator]
                else:
                    result = Calculate(operand1,operand2,operator)
                    self.queue.append((operand1,operand2,operator))
                    self.cache[operand1][operand2][operator] = result
                    self.count+=1
            else:
                result = Calculate(operand1,operand2,operator)
                self.queue.append((operand1,operand2,operator))
                self.cache[operand1][operand2] = {operator:result}
                self.count+=1
        else:
            result = Calculate(operand1,operand2,operator)
            self.queue.append((operand1,operand2,operator))
            self.cache[operand1]={operand2:{operator:result}}
            self.count+=1
            if self.count > 2:
                item = self.queue.popleft()
                del self.cache[item[0]][item[1]][item[2]]
                self.count-=1
                if not self.cache[item[0]][item[1]]:
                    del self.cache[item[0]][item[1]]
                    if not self.cache[item[0]]:
                        del self.cache[item[0]]
        return result


global cache 
cache = Cache()
def Calculate(operand1,operand2,operator):
        if operator == '+':
            result = operand1 + operand2
        elif operator == '-':
            result = operand1 - operand2
        elif operator == '*':
            result = operand1 * operand2
        elif operator == '/':
            result = operand1 / operand2
        elif operator == '%':
            result = operand1 % operand2
        elif operator == '^':
            result = operand1 ** operand2
        else:
            result = None
        return str(result)

@app.route('/', methods=['POST'])
def calculate():
    global cache

    operand1 = float(request.form["operand1"])
    operand2 = float(request.form["operand2"])
    operator = request.form["operator"]

    print cache.cache
    return cache.check(operand1,operand2,operator)


if __name__ == '__main__':
    app.run(debug=True)