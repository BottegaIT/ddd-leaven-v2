ddd-leaven-v2
=============

DDD-CqRS sample v2.0 project that helps you with starting out advanced domain modeling using Spring, JPA and testing.

Wiki is still under construction, bout you can use this "map" to navigate through the code: http://prezi.com/akrfq7jyau8w/ddd-cqrs-leaven-v20/

Group: https://groups.google.com/forum/#!forum/ddd-cqrs-sample
Blog: http://ddd-cqrs-leaven.blogspot.com/


##Another DDD and CqRS Sample?

Primary goals of this project are the following: 
* presenting sample implementation of all DDD Building Blocks and techniques - no technical compromises, real world problems and solutions 
* presenting advanced lingustic techniques that are usefull for exploring Domian Exper knowledge during Modeling Wirlpool sessions
* providing well crafted code, ready to be utilized in production 
* presenting ready ready to use and easy to adopt tools and best practices 


##What is Leaven idea?

Our intention is to provide a leaven ([leavening agent](http://en.wikipedia.org/wiki/Leavening_agent)) - something that you use to make bread - a good one. 


##You are the Architect!

So you take our leaven, understand it deeply and modify to fit your context. 

You don't need to couple your code with the leaven code. You can, but don't have to extend our classes. Better approach is to change, rename and repackage leaven classes:. 

Leaven is really simple and small. We achieved this by developing straightforward code without unnecessary abstraction-distraction like XML, and inner accidental complexity typical for frameowrks. 

If You want to change something then go straight to the code and do it instead of reading this documentation. You are the Architect! 
Noninvasive philosophy

Our goal is to prepare a business developer programming model (way of thinking about class-level design) that is free of any platform-specific solutions. 

Business developer should focus on analysis and domain modeling - engine does technical stuff. 
Portable architecture - technical independence

Although the implementation is based on Spring and JPA we managed to avoid any special approach or programming model. Therefore our architecture is portable which means You can implement this "style" using any Java framework or platform (EJB, etc).
