import matplotlib.pyplot as plt
import numpy as np

f = open("hard-report-2-sData.txt",'r')
f1 = open('hard-report-2-data.txt','r')
fv = open('hard-report-2-sVel.txt', 'r')

x = []
y = []
time = []

for line in f:
    split = line.split()
    time.append(float(split[0]))
    
    x.append(float(split[1]))
    y.append(float(split[2]))
    
plt.figure()
plt.plot(time, x, 'r-')
plt.plot(time, y, 'g-')

time1 = []
x1= []
y1= []
for line in f1:
    split = line.split()
    time1.append(float(split[0]))
    x1.append(float(split[1]))
    y1.append(float(split[2]))

plt.plot(time1, x1, 'c.')
plt.plot(time1, y1, 'y.')


xv = []
yv = []

#for line in fv:
#    split = line.split()
#    time.append(float(split[0]))
#    
#    xv.append(float(split[1]))
#    yv.append(float(split[2]))
#    
#plt.plot(time1, xv)
#plt.plot(time1, yv)

plt.show()