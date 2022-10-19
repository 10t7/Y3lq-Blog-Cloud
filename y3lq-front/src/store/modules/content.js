export default {
  namespaced: true,

  state: {
    // posts: [
    //   {
    //     title: '极简主义风格',
    //     createTime: '2022-08-01',
    //     author: 'Y3lq',
    //     summary: `极简主义（Minimalism）可以定义为：当一件作品的内容被减至最低限度时所散发的完美感觉。当物体的所有组成部分、`,
    //     body: `
    //       <p>极简主义（Minimalism）可以定义为：当一件作品的内容被减至最低限度时所散发的完美感觉。当物体的所有组成部分、所有细节以及所有的连接都被减少压缩至精华时，他就会拥有这种特性，这就是去掉非本质元素的结果。</p>

    //       <p>极简主义产生于20世纪60年代，是20世纪现代艺术重要流派之一。极简主义的设计遵循“少即是多”的设计理念，对建筑设计、装饰设计、时尚和绘画等诸多艺术领域都产生了深远的影响。

    //       现代生活的快节奏和重负荷，以及海量碎片化信息使人们内心越来越焦虑，人们渴望内心的安宁，渴望缓解精神压力，解除审美疲劳。极简主义理性实用、简约整洁、直观易懂、优雅大方等特点，正好迎合了人们的精神需求，被社会大众所青睐，在当下被越来越多的人们所追捧和喜爱。

    //       例如，现在人们所追崇的北欧风格日式风格设计、扁平化设计，以及“断舍离”生活方式，其本质都是极简主义。</p>

    //       <p>虽然极简主义设计以简约著称，但事实上极简主义设计并非一味追求设计形式的简化，而是追求设计形式和功能的平衡。即在实现设计功能的前提下，去除非本质的、不必要的装饰，使用干净流畅的外形，使设计呈现出优雅感和纯粹感，减少人们的认知障碍，方便人们使用与欣赏。

    //       </p>

    //       <p>正如著名建筑设计师迪特·拉姆斯（Dieter Rams）的设计理念“Less，but better”一样，极简设计的设计核心理念同样是希望通过简约的设计给人们带来更好的使用感，即简约但富有意义。

    //       为了做到这一点，极简主义所需要做的不仅仅是简化与剔除元素，而是精准与确定功能。因而在极简主义设计简约的表面下，隐藏着反而是复杂的设计工序。</p>
    //     `,
    //     tags: ['极简', '极简主义'],
    //     // more: `/article/demo-post`,
    //     slug: '极简',
    //   },
    // {
    //   title: 'Hello world!',
    //   date: 'December 21, 2018',
    //   author: 'John Leider',
    //   body: `
    //     <p>Adipiscing enim eu turpis egestas pretium aenean pharetra magna. Sagittis id consectetur purus ut faucibus. Viverra justo nec ultrices dui sapien eget mi.</p>
    //   `,
    //   slug: 'hello-world',
    // },
    // {
    //   title: 'Lorem Ipsum',
    //   date: 'December 20, 2018',
    //   author: 'John Leider',
    //   body: `
    //     <p>Adipiscing enim eu turpis egestas pretium aenean pharetra magna. Sagittis id consectetur purus ut faucibus. Viverra justo nec ultrices dui sapien eget mi.</p>
    //   `,
    //   slug: 'lorem-ipsum',
    // },
    // {
    //   title: 'Great news!',
    //   date: 'December 14, 2018',
    //   author: 'John Leider',
    //   body: `
    //     <p>Adipiscing enim eu turpis egestas pretium aenean pharetra magna. Sagittis id consectetur purus ut faucibus. Viverra justo nec ultrices dui sapien eget mi.</p>
    //   `,
    //   slug: 'great-news',
    // },
    // ],
    posts: [],
  },
  mutations: {
    SAVE_ARTICLELIST(state, list) {
      state.posts = list
    },

  },


}
