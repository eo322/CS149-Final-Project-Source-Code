#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>

#define MAX 100
// global variables are shared by threads
int array[MAX];
int array_size = 0;

//create a data structure using a struct
typedef struct {
    int low;
    int high;
} parameters;

int cmp(const void  *a, const void *b) {
    return *((int *)a) - *((int *)b);
}

// qsort function will do the sorting
void *runner(void *param) {
    int low, high;
    low = ((parameters *)param)->low;
    high = ((parameters *)param)->high;
    qsort(array + low, high - low, sizeof(int), cmp);
    pthread_exit(0);
}

void in_list() {
    printf("Please enter the number of elements:");
    scanf("%d", &array_size);
    for(int i = 0; i != array_size; ++i) {
        scanf("%d", &array[i]);
    }
}

void merge_list(int *result) {
    int low1 = 0, high1 = array_size / 2;
    int low2 = array_size / 2, high2 = array_size;
    int i = 0;
    while(low1 < high1 && low2 < high2) {
        if(array[low1] < array[low2]) {
            result[i++] = array[low1++];
        } else {
            result[i++] = array[low2++];
        }
    }
    if(low2 < high2) {
        low1 = low2, high1 = high2;
    }
    while(low1 < high1) {
        result[i++] = array[low1++];
    }
}

void print_list(int *arr, int size) {
    for(int i = 0; i != size; ++i) {
        printf("%d ", arr[i]);
    }
    printf("\n");
}

int main() {
    pthread_t tid[2];
    pthread_attr_t attr;
    in_list();
    printf("original list:\n");
    print_list(array, array_size);
    parameters data[2];
    data[0].low = 0, data[0].high = array_size / 2;
    data[1].low = array_size / 2, data[1].high = array_size;
    pthread_attr_init(&attr);
    // sorting threads
    for(int i = 0; i != 2; ++i) {
        pthread_create(&tid[i], &attr, runner, &data[i]);
    }
    // sorting threads results passed back to main thread
    for(int i = 0; i != 2; ++i) {
        pthread_join(tid[i], NULL);
    }
    printf("sorting thread 0:\n");
    print_list(array, array_size / 2);
    printf("sorting thread 1:\n");
    print_list(array + array_size / 2, array_size - array_size / 2);
    int *sorted_list = malloc(sizeof(int) * array_size);
    merge_list(sorted_list);
    printf("merge thread:\n");
    print_list(sorted_list, array_size);
    return 0;
}