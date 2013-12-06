--
-- Copyright 2011-2014 the original author or authors.
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--     http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

insert into Client(aggregateId, aggregateStatus, version, name) values('1', 0, 1, 'client 1');
insert into Product(aggregateId, aggregateStatus, version, name, productType, currencyCode, denomination) values('p1', 0, 1, 'product 1', 'STANDARD', 'EUR', 10);
insert into Product(aggregateId, aggregateStatus, version, name, productType, currencyCode, denomination) values('p2', 0, 1, 'product 2', 'STANDARD', 'EUR', 20);
