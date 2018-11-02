close

I = imread('test0.png');

%task = 'letters'; % letters/words
task = 'words'; % letters/words

filter = true;
noise = 'salt & pepper';
%noise = 'gaussian';

if (filter)
    I = imnoise(I, noise);
end

size_of_disk = 10;
SE = strel('disk', size_of_disk);

img_bw = im2bw(I);

img_comple = imcomplement(img_bw);

img_to_proc = img_comple;

% NOISE
if (strcmp(noise, 'salt & pepper'))
    img_to_proc = medfilt2(img_comple);
elseif (strcmp(noise, 'gaussian'))
    % just do nothing
else
    'No noise on the picture'
end

% IF WORDS WHAT U WANT
if (strcmp(task, 'words'))
    img_to_proc = imclose(img_to_proc, SE);
end

prop = regionprops(img_to_proc, 'BoundingBox');
BB = cat(1, prop.BoundingBox);


%imshow(img);for i=1:size(BB,1), rectangle('Position', BB(i,:),'EdgeColor','r', 'LineWidth', 1, 'LineStyle', '-');end

%imshow(img);
for i=1:size(BB,1)
    rect(i) = rectangle('Position', BB(i,:),'EdgeColor','black', 'LineWidth', 1, 'LineStyle', '-');
end

MASK = logical(size(img_bw));
[x,y,z] = size(I);

%x = 40
%y = 200
[a,b] = size(rect);

for i=1:b
    k = i*6;
    
    low_left_x = rect(i).Position(1);
    up_left_x = low_left_x;
    up_right_x = rect(i).Position(1) + rect(i).Position(3);
    low_right_x = up_right_x;
    
    low_left_y = rect(i).Position(2);
    up_left_y = low_left_y;
    up_right_y = rect(i).Position(2) + rect(i).Position(4);
    low_right_y = up_right_y;      
    
    x_rect(k-5) = low_left_x;
    x_rect(k-4) = low_right_x;
    x_rect(k-3) = up_right_x;
    x_rect(k-2) = up_left_x;
    x_rect(k-1) = low_left_x;
    x_rect(k-0) = NaN;
    
    y_rect(k-5) = low_left_y;
    y_rect(k-4) = up_left_y;
    y_rect(k-3) = low_right_y;
    y_rect(k-2) = up_right_y;
    y_rect(k-1) = low_left_y;
    y_rect(k-0) = NaN;
end
x_rect(end) = [];
y_rect(end) = [];


k = 1;
for i = 1 : x
    for j = 1 : y
        yq(k) = i;
        xq(k) = j;
        k = k+1;
    end
end

in = inpolygon(xq, yq, x_rect, y_rect);


MASK(yq(~in), xq(~in)) = 0;
MASK(yq(in), xq(in)) = 1;



% imshow(MASK);

    IR = zeros(size(I)); 
    [x,y,z] = size(I);
    

    figure; imshow(I);
    
    for i = 1 : x-1
        for j = 1 : y-1
            if MASK(i,j) == 0
                IR(i,j,:) = I(i,j,:);
            elseif MASK(i,j) == 1
                if img_bw(i,j) == 0 %BW ben betû van
                    IR(i,j,:) = img_bw(i,j,:);
                else
                    IR(i,j,:) = [128,128,0];
                end
            end
        end        
    end
figure; imshow(IR);