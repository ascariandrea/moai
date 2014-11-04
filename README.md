## MOAI - The Android FrameWork

The Android FrameWork is a framework to speed up the android development, providing simple APIs to build applications that interacts with RESTful api services.

## Usage
You can include this framework as library in your project, or you can start from the boilerplate I've created for intelliJ and gradle environment by cloning the boilerplate [repo](https://github.com/ascariandrea/moai-boilerplate).
```
$ git clone git@github.com:ascariandrea/moai-boilerplate.git my-new-project
```
and follow the instruction in the boilerplate README file.

## Components
* [Activities](https://github.com/ascariandrea/moai/wiki/Activities)
* [Fragments](https://github.com/ascariandrea/moai/wiki/Fragments)
* [Models](https://github.com/ascariandrea/moai/wiki/Models)
* [Utils](https://github.com/ascariandrea/moai/wiki/Utils)
* [Views](https://github.com/ascariandrea/moai/wiki/Views)


## Samples

* [Simple Social Login](https://github.com/ascariandrea/moai/tree/master/sample/src/main/java/com/ascariandrea/moai/samples/login)

## Activities
To be documented.

## Fragments

There are three principal classes of fragments:

* InjectedFragment
* InjectedResourceFragment
* InjectedListFragment

#### InjectedFragment
This class provide methods to run your logic in specific fragment life cycle points.
Such as `onViewsInjected()` that permits to run code right after views injection by android annotations.


#### InjectedResourceFragment
This class is more complete than the above one. You can attach a resource to the fragment and avoid a full manual
setup to fetch the resource from the API server, you can simply declare your fragment as:

```java
public class ActivitiesFragment extends InjectedResourceFragments<Activity> {
  
}
```

Assuming that your `Activity` class is a subclass of Moai `Model` class.

#### InjectedListFragment
As the `InjectedResourceFragment` permits to deal with your single resource through your api service, this fragment do the same with resources collections.


## Model & ModelCollection

The `Model` & `ModelCollection` are classes that made simple json deserialization and serialization and passing well formed payload to your backend.

```java
@JsonRootValue("activity")
public Activity extends Model {
  
  @JsonProperty("id")
  public int id;

  @JsonProperty("text")
  public String text;

}
```

You can map your json payload properties to class property and don't care about anything else.
The `ModelCollection` is committed to handle json collection and porting them to `List<Model>` and vice versa.


## Contributing

Any help in project development and documentation improvement will be appreciated.

<!-- Detailed documentation in [Wiki](https://github.com/ascariandrea/moai/wiki). -->


