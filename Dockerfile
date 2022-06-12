FROM ubuntu:22.04
EXPOSE 80

RUN apt-get update -y && \
    apt-get install -y python3.10 python3-pip

RUN pip install pillow
RUN mkdir img

COPY ./requirements.txt /app/requirements.txt

WORKDIR /app

RUN pip install -r requirements.txt

COPY . /app

ENTRYPOINT [ "python3" ]

CMD [ "app.py" ]