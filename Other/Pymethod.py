# -- coding: utf-8 -
import time
import cv2
import sys
import urllib.request
import numpy
import requests
import json
from selenium import webdriver
from matplotlib import pyplot
from selenium.webdriver import ActionChains
from selenium.webdriver.common.keys import Keys
options = webdriver.FirefoxOptions()
options.add_argument('--auto-open-devtools-for-tabs')
options.headless = True
options.add_argument('window-size=1920,1080')
driver = webdriver.Firefox(options=options)
headers = {

    'User-Agent':
        'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/73.0.3683.75 '
        'Chrome/73.0.3683.75 Safari/537.36',
    "Accept-Encoding": "gzip, deflate",
    'Accept': 'application/json;charset=UTF-8',
    'Content-Type': 'application/json;charset=UTF-8'
}


def login():
    driver.get('https://zoj.pintia.cn/auth/login')
    driver.implicitly_wait(10)
    driver.find_element_by_name('email').send_keys('1940977566@qq.com')
    driver.find_element_by_name('password').send_keys('acm12345678')
    driver.find_element_by_class_name('cur-p').click()
    time.sleep(1)
    while True:
        img_bg = None
        img_saw = None
        while img_bg is None:
            img_bg = driver.find_element_by_class_name(
                'yidun_bg-img').get_attribute('src')
            img_saw = driver.find_element_by_class_name(
                'yidun_jigsaw').get_attribute('src')
        time.sleep(0.5)

        def get_img(url):
            img = None
            while True:
                try:
                    resp = urllib.request.urlopen(url)
                    img = numpy.asarray(bytearray(resp.read()), dtype='uint8')
                    img = cv2.imdecode(img, cv2.IMREAD_COLOR)
                except AttributeError:
                    continue
                break
            return img

        urllib.request.urlretrieve(img_bg, './bg.jpg')
        urllib.request.urlretrieve(img_saw, './saw.png')
        bg = get_img(img_bg)
        gray = cv2.cvtColor(cv2.imread('./bg.jpg'), cv2.COLOR_BGR2GRAY)
        ret, gray = cv2.threshold(gray, 190, 255, cv2.THRESH_BINARY)
        gray = cv2.Laplacian(gray, cv2.CV_16S, ksize=3)
        gray = cv2.convertScaleAbs(gray)
        gray = numpy.array(gray, dtype='float')
        exp = cv2.imread('./saw.png', cv2.IMREAD_GRAYSCALE)
        exp = cv2.Laplacian(exp, -1, ksize=3)
        ret, exp = cv2.threshold(exp, 240, 255, cv2.THRESH_BINARY)
        exp = numpy.array(exp, dtype=float)
        pyplot.imshow(exp, cmap='gray')
        output = cv2.filter2D(gray, -1, exp)
        output = (output - output.min()) / (output.max() - output.min()) * 255
        pos = numpy.unravel_index(output.argmax(), output.shape)
        slide = driver.find_element_by_class_name('yidun_slider')
        ActionChains(driver).drag_and_drop_by_offset(
            source=slide, xoffset=pos[1] - 17, yoffset=0).perform()
        time.sleep(1)
        cache = driver.execute_script(
            "return localStorage.getItem('user-cache')")
        cache = json.loads(cache)

        try:
            if cache['userId'] is not None:
                return cache['userId']
        except KeyError as e:
            pass


def submit(code, problem_id, exam_id):
    problem = 'https://zoj.pintia.cn/problem-sets/91827364500/problems/%s' % problem_id
    driver.get(problem)
    time.sleep(0.5)
    #####
    data = {"details": [{"problemSetProblemId": "%s" % problem_id, "programmingSubmissionDetail": {"compiler": "GCC",
                                                                                                   "program": "%s" % code}}]}
    data = json.dumps(data)
    temp = requests.post('https://zoj.pintia.cn/api/problem-sets/91827364500/submissions?exam_id=%s' % exam_id,
                         data=data,
                         headers=headers, cookies=cookies1)
    time.sleep(1)
    data = json.loads(temp.text)
    pid = data['submissionId']
    queued = 0
    qqq = ""
    while queued == 0:
        qqq = requests.get("https://pintia.cn/api/submissions/%s?preview_submission=false" % str(pid), headers=headers,
                           cookies=cookies1)
        time.sleep(0.3)
        queued = json.loads(qqq.text)['queued']
    return qqq.text


cookies1 = {}


def get_cook():
    cookies = driver.get_cookies()
    for cookie in cookies:
        cookies1[cookie['name']] = cookie['value']


if __name__ == "__main__":
    login()
    get_cook()
    # 从sys中获取参数
    problem_id = sys.argv[1]
    code = sys.argv[2]
    print(problem_id)
    print(code)
    req = requests.get('https://zoj.pintia.cn/api/problem-sets/91827364500/exams',
                       headers=headers, cookies=cookies1)
    data = json.loads(req.content.decode('utf-8'))
    exam_id = data['exam']['id']
    res = submit(code, problem_id, exam_id)
    print(res)
