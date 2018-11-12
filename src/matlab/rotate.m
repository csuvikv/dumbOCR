I = imread('test0.png');
J = imrotate(I, 10, 'nearest', 'loose');

imshow(J)

'asdasd'

imwrite(J,'test0_r.png');