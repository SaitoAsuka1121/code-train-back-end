import time
import json
import requests
import pymysql


# 取题目信息
def get_problem(id):
    problem_id = str(id)
    r = requests.get("https://pintia.cn/api/problem-sets/91827364500/problems/%s" % problem_id, headers=headers)
    time.sleep(0.3)
    data = json.loads(r.content.decode('utf-8'))
    print(data)
    content = data['problemSetProblem']['content']
    title = data['problemSetProblem']['title']
    memory_limit = data['problemSetProblem']['problemConfig']['programmingProblemConfig']['memoryLimit']
    time_limit = data['problemSetProblem']['problemConfig']['programmingProblemConfig']['timeLimit']
    id = data['problemSetProblem']['id']
    try:
        cursor.execute('insert into t_problems(pid,body,title,memory,time)'
                       'values(%s,%s,%s,%s,%s)', (id,content, title, memory_limit, time_limit))
        conn.commit()
        print('插入成功')
    except Exception as e:
        print(str(e))  # 错误
        conn.rollback()
        print('插入失败')

headers = {
    # 假装自己是浏览器
    'User-Agent':
        'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/73.0.3683.75 '
        'Chrome/73.0.3683.75 Safari/537.36',
    "Accept-Encoding": "gzip, deflate",
    'Accept': 'application/json;charset=UTF-8'
}
conn = pymysql.connect(host='dreamstartcloud.top', user='root', password='!12Mengqiyun', database='codetrain-data')
cursor = conn.cursor()



# 页码
for num in range(0, 32):  # 页数（0-30）
    page_id = str(num)
    response = requests.get(
        "https://pintia.cn/api/problem-sets/91827364500/problem-list?exam_id=0&problem_type=PROGRAMMING&page=%s&limit"
        "=100" % page_id,
        headers=headers)
    time.sleep(0.5)
    data = json.loads(response.content.decode('utf-8'))
    for problem in data['problemSetProblems']:
        get_problem(problem['id'])
conn.close()
