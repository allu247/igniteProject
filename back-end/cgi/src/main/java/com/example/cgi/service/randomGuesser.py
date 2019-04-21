import sys
import random

class myPythonClass:
    @staticmethod
    def randomGuesser(filename):


         random.seed(filename)
         category_nr = random.randint(1, 10)
         return "Category " + str(category_nr)




if __name__ == '__main__':
    print(myPythonClass.randomGuesser(sys.argv[1]))
