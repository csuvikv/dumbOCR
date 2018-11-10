function main (impathIN, task, impathOUT, hex_code)
    close

%         impathIN = 'testhard.png';
%         task = 'words';
%  
%         impathOUT = 'impathOUT1.png';
%         hex_code = '#f442e8';

    I = imread(impathIN);

    %task = 'letters'; % letters/words/lines

    filter = false;
    noise = '';

    if (filter)
        noise = 'salt & pepper';
        %noise = 'gaussian';
        I = imnoise(I, noise);
    end

    img_bw = im2bw(I, 0.80);

    %figure; imshow(img_bw)

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

    prop = regionprops(img_to_proc, 'BoundingBox');
    BB = cat(1, prop.BoundingBox);

    % IF WORDS OR LETTERS
    if (strcmp(task, 'words'))

        dist_b_letters = [];
        
        min_W = 999999999;
        for i=1:size(BB,1)
            for j=i+1:size(BB,1)
                temp = abs((BB(i,2)+BB(i,4))-BB(j,2));
                if temp<min_W
                    min_W = temp;
                end
            end
            dist_b_letters(i) = min_W;
        end
        
        size_of_SE = round(max(dist_b_letters)); %5 for testhard.png
        SE = strel('disk', size_of_SE);
        img_to_proc = imclose(img_to_proc, SE);
    elseif (strcmp(task, 'letters'))

        % FIND OUT THE DIST BETWEEN LETTERS
        min_W = 999999999;
        for i=1:size(BB,1)
            for j=i+1:size(BB,1)
                temp = abs((BB(i,2)+BB(i,4))-BB(j,2));
                if temp<min_W
                    min_W = temp;
                end
            end
        end
        min_H = 999999999;
        for i=1:size(BB,1)
            for j=i+1:size(BB,1)
                temp = abs((BB(i,1)-BB(i,3))-BB(j,1));
                if temp<min_H
                    min_H = temp;
                end
            end
        end

        if (min_W == 0)
            min_W=min_W+1;
        end

        size_of_SE = [min_H+3, min_W];
        SE = strel('rectangle', size_of_SE);
        img_to_proc = imclose(img_to_proc, SE);
        
    elseif (strcmp(task, 'lines'))
        size_of = max(BB(:,4));
        size_of = round(size_of);
        
        min_H = 999999999;
        for i=1:size(BB,1)
            for j=i+1:size(BB,1)
                temp = abs((BB(i,1)-BB(i,3))-BB(j,1));
                if temp<min_H
                    min_H = temp;
                end
            end
        end
        
        size_of_SE = [min_H+3, size_of];
        SE = strel('rectangle', size_of_SE);
        img_to_proc = imclose(img_to_proc, SE);
        
    end

    %imshow(img_to_proc);
    
    prop = regionprops(img_to_proc, 'BoundingBox');
    BB = cat(1, prop.BoundingBox);

    BB = round(BB);

    color = hex2rgb(hex_code, 256);

    % SHOW THE RESULT
    if (strcmp(task, 'words') || strcmp(task, 'letters') || strcmp(task, 'lines'))
%         imshow(I);
        for i=1:size(BB,1)
%             rectangle('Position', BB(i,:),'EdgeColor','r', 'LineWidth', 1, 'LineStyle', '-');
            
            % PAINT IT BE
            for k = 1:length(color)

                I(BB(i,2), BB(i,1):BB(i,3)+BB(i,1),k) = color(k);
                I(BB(i,2):BB(i,4)+BB(i,2), BB(i,1),k) = color(k);

                I(BB(i,2)+BB(i,4), BB(i,1):BB(i,3)+BB(i,1),k) = color(k);
                I(BB(i,2):BB(i,4)+BB(i,2), BB(i,1)+BB(i,3),k) = color(k);

           end
        end
        %SAVE
        %figure; imshow(I);
        imwrite(I, impathOUT)
    end

    if (strcmp(task, 'linesOFF'))
        %imshow(img);
        %{
            for i=1:size(BB,1)
                rect(i) = rectangle('Position', BB(i,:),'EdgeColor','black', 'LineWidth', 1, 'LineStyle', '-');
            end
        %}

        MASK = logical(size(img_bw));
        [x,y] = size(I);

        for i=1:size(BB,1)
            k = i*6;
            low_left_x = BB(i,1);
            up_left_x = low_left_x;
            up_right_x = BB(i,1) + BB(i,3);
            low_right_x = up_right_x;

            low_left_y = BB(i,2);
            up_left_y = low_left_y;
            up_right_y = BB(i,2) + BB(i,4);
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


        %figure; imshow(I);

        for i = 1 : x-1
            for j = 1 : y-1
                if MASK(i,j) == 0
                    IR(i,j,:) = I(i,j,:);
                elseif MASK(i,j) == 1
                    if img_bw(i,j) == 0 %BW ben betû van
                        IR(i,j,:) = img_bw(i,j,:);
                    else
                        IR(i,j,:) = color; %TODO: does it work?
                    end
                end
            end
        end
        %figure; imshow(IR);
        imwrite(IR, impathOUT)
    end
end