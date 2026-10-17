

FROM openjdk

RUN microdnf install -y unzip \
    && curl -Ssl https://downloads.gauge.org/stable | sh

WORKDIR /gauge
 
COPY . .

RUN gauge install \
    && gauge install screenshot

CMD ["gauge", "run",  "specs"]
