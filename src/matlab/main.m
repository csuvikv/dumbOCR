%UNTITLED Summary of this function goes here
%   Detailed explanation goes here

    close
    
    image_name = 'test0.png';
    size_of_disk = 10;
    bw_threshold = 0.5;
    
    
    I = imread(image_name);
    
    J = imread(image_name);
    
    SE = strel('disk', size_of_disk);
    
    IBW = im2bw(I, bw_threshold);
    

    
    
    
    % erode->dilate %nyitás
    J = imerode(J, SE);
    J = imdilate(J, SE);
    
    BW = im2bw(J, bw_threshold);
    BW = imcomplement(BW);
    
    conn = [0,1,0;1,1,1;0,1,0];
    
    CH = bwconvhull(BW, 'objects', conn);
   
    subplot(2,3,1); imshow(I);
    subplot(2,3,2); imshow(J);
    subplot(2,3,3); imshow(BW);
    subplot(2,3,4); imshow(CH); 
    
    
    
    
    IR = zeros(size(I)); 
    [x,y,z] = size(I);
    
    for i = 1 : x-1
        for j = 1 : y-1
            if CH(i,j) == 0
                IR(i,j,:) = I(i,j,:);
            elseif CH(i,j) == 1
                if IBW(i,j) == 0 %BW ben betû van
                    IR(i,j,:) = IBW(i,j,:);
                else
                    IR(i,j,:) = [128,128,0];
                end
            end
        end        
    end
    

    subplot(2,3,5); imshow(I);
    subplot(2,3,6); imshow(IR);
   
