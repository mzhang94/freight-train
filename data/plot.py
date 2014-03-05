#!/usr/bin/python

import matplotlib.pyplot as plt
import math
import sys

if __name__ == "__main__":
    if len(sys.argv) <= 1:
        filename = "reports"
    else:
        filename = sys.argv[1]
    f = open(filename + "-data.txt",'r')
    f1 = open(filename + '-sData.txt','r')
    fv = open(filename + '-sVel.txt', 'r')
    
    x = []
    y = []
    time = []
    
    xFalse = []
    yFalse = []
    timeFalse = []
    for line in f:
        split = line.split()
        if split[3] =='true':
            time.append(float(split[0]))    
            x.append(float(split[1]))
            y.append(float(split[2]))
        else:
            timeFalse.append(float(split[0]))    
            xFalse.append(float(split[1]))
            yFalse.append(float(split[2]))
    f.close()    
    
    plt.figure()
    plt.plot(time, x, 'c.')
    plt.plot(time, y, 'y.')
    plt.plot(timeFalse, xFalse, 'r.')
    plt.plot(timeFalse, yFalse, 'r.') 
    
    time1 = []
    x1= []
    y1= []
    for line in f1:
        split = line.split()
        time1.append(float(split[0]))
        x1.append(float(split[1]))
        y1.append(float(split[2]))
    f1.close()
    lx = plt.plot(time1, x1, 'r-', label = "estimate x")
    ly = plt.plot(time1, y1, 'g-', label = "estiamte y")


xv = []
yv = []
v = []
timev = []

for line in fv:
    split = line.split()
    timev.append(float(split[0]))
    
    xv.append(float(split[1]))
    yv.append(float(split[2]))
    v.append(math.sqrt(float(split[1])**2 + float(split[2])**2))
    
fv.close()
plt.plot(timev, xv, label = "vel x")
plt.plot(timev, yv, label = "vel y")
plt.plot(timev, v, label = "speed")

plt.legend(loc = 7)
#plt.legend([lxv, lyv], ["velocity in x", "velocity in y"])
plt.show()