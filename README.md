## AFW - Android FrameWork

The Android FrameWork is a framework to speed up the android development, providing simple APIs to build applications that interacts with RESTful api services.

## Usage
You can include this framework as library in your project, or you can start from the boilerplate I've created for intelliJ and gradle environment by cloning the boilerplate repo
```
$ git clone git@github.com:ascariandrea/afw-boilerplate.git my-new-project
```
and follow the instruction in the boilerplate README file.

## Components
* [Activities](https://github.com/ascariandrea/afw/wiki/Activities)
* [Fragments](https://github.com/ascariandrea/afw/wiki/Fragments)
* [Models](https://github.com/ascariandrea/afw/wiki/Models)
* [Utils](https://github.com/ascariandrea/afw/wiki/Utils)
* [Views](https://github.com/ascariandrea/afw/wiki/Views)


## Samples

* [Simple Social Login](https://github.com/ascariandrea/afw/tree/master/sample/src/main/java/com/ascariandrea/afw/samples/login)



### Fragments

There are three principal classes of fragments:

* InjectedFragment
* InjectedResourceFragment
* InjectedListFragment

#### InjectedFragment
This class provide methods to run your logic in specific fragment life cycle points.
Such as `onViewsInjected()` that permits to run code right after views injection by android annotations.


#### InjectedResourceFragment
This class is more complete than the above one.

## Contributors


Detailed documentation in [Wiki](https://github.com/ascariandrea/afw/wiki).


