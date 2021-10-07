# the-olympics: Data Integration project

The domain we have decided to choose for this project is the domain of the Olympic Games. Many factors influenced our decision. We both love sport for what it represents in its purest form, namely a psycho-physical challenge undertaken by an athlete willing to push himself beyond his limits, competing with other athletes in a respectful environment.

We think the Olympic Games are the perfect representation of such a spirit. Even today, in the Olympic Oath, pronounced by an athlete, coach and judge at the Opening Ceremony, there are references to fair play, respect and sport spirit.

Being so passionate about this topic, we have looked for possible datasets and we have found many of them, containing partial information, that suit perfectly the task of data integration. Observing the datasets we have found how the domain is composed by a central entity, clearly the Olympic Game, connected to different disciplines and events present at that particular Game. Moreover, data about the medals won at the Game related the athletes (belonging to a country) to the different games, events and medals.

Moreover, each Olympic Game has the olympic torch, which, starting from the city of Olympia, traverses many different countries in a route that ends at the hosting city, another information we have modelled.

One of the main aspects that reflects the variety of the data, is represented by the fact that Olympics are divided in winter and summer games, with different datasets that aim at representing only one of the two types. The information is therefore spread across different data sources with different schema, coverage and structure. The objective of our data integration project is to develop a high-level abstraction of the domain, collecting together data from the different sources and offering to the user an interface to query and retrieve information about Olympic Games.
